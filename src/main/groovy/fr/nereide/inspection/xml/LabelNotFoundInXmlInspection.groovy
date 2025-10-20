package fr.nereide.inspection.xml

import static fr.nereide.project.utils.MiscUtils.getUiLabelSafeValue

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttribute
import fr.nereide.inspection.InspectionBundle
import fr.nereide.inspection.common.OfbizBaseInspection
import fr.nereide.inspection.quickfix.xml.CreateMissingLabelFix
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.pattern.OfbizXmlPatterns
import fr.nereide.project.utils.UiLabelTextRange
import org.jetbrains.annotations.NotNull

/**
 * Checks labels exists
 */
class LabelNotFoundInXmlInspection extends OfbizBaseInspection {

    CreateMissingLabelFix myQuickFix = new CreateMissingLabelFix()

    /* codenarc-disable FactoryMethodName, UnusedMethodParameter */

    PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        /* codenarc-enable FactoryMethodName UnusedMethodParameter */
        return new XmlElementVisitor() {

            void visitXmlAttribute(@NotNull XmlAttribute attribute) {
                if (PluginActivator.getInstance(attribute.project).inactive) return
                if (!OfbizXmlPatterns.LABEL_CALL.accepts(attribute.valueElement)) return
                OfbizProjectHelper ph = OfbizProjectHelper.getInstance(attribute.project)
                String labelName = attribute.value
                if (!ph.getProperty(getUiLabelSafeValue(labelName)))
                    holder.registerProblem(
                            attribute,
                            InspectionBundle.message('inspection.label.not.found.display.descriptor'),
                            ProblemHighlightType.WARNING,
                            new UiLabelTextRange(attribute),
                            myQuickFix)
            }

        }
    }

}
