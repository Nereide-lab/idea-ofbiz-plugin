package fr.nereide.inspection.xml

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttribute
import fr.nereide.inspection.InspectionBundle
import fr.nereide.inspection.quickfix.xml.CreateMissingLabelFix
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.pattern.OfbizXmlPatterns
import org.jetbrains.annotations.NotNull

import static fr.nereide.project.utils.MiscUtils.getUiLabelSafeValue

class LabelNotFoundInXmlInspection extends LocalInspectionTool {

    CreateMissingLabelFix myQuickFix = new CreateMissingLabelFix()

    @Override
    boolean isEnabledByDefault() {
        return true
    }

    @Override
    @NotNull
    PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {

        return new XmlElementVisitor() {
            @Override
            void visitXmlAttribute(@NotNull XmlAttribute attribute) {
                if (!OfbizXmlPatterns.LABEL_CALL.accepts(attribute.getValueElement())) return
                ProjectServiceInterface ps = attribute.getProject().getService(ProjectServiceInterface.class)
                String labelName = attribute.value
                if (!ps.getProperty(getUiLabelSafeValue(labelName)))
                    holder.registerProblem(
                            attribute,
                            InspectionBundle.message('inspection.label.not.found.display.descriptor'),
                            ProblemHighlightType.WARNING,
                            myQuickFix)
            }
        }
    }
}
