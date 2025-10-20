package fr.nereide.inspection.xml

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static fr.nereide.inspection.InspectionBundle.message

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import fr.nereide.inspection.common.OfbizBaseInspection
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.pattern.OfbizXmlPatterns
import fr.nereide.reference.xml.ScreenReference
import org.jetbrains.annotations.NotNull

/**
 * Basic inspection for screens duplicates
 */
class DuplicatedScreenInspection extends OfbizBaseInspection {

    static boolean isDuplicate(XmlAttributeValue val, boolean isDef) {
        XmlElement valToUse = isDef ? val : new ScreenReference(val, true).resolve() as XmlElement
        if (!valToUse) return false
        return OfbizProjectHelper.getInstance(val.project)
                .collectAllScreenFromCurrentFileFromElement(valToUse)
                .findAll { screen -> screen.name.value == val.value }
                .size() > 1
    }

    @Override
    @NotNull
    PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new XmlElementVisitor() {

            @Override
            void visitXmlAttributeValue(@NotNull XmlAttributeValue val) {
                if (PluginActivator.getInstance(val.project).inactive) return
                boolean isDefinition = OfbizXmlPatterns.SCREEN_NAME_IN_DEFINITION.accepts(val)
                if (!isDefinition && !OfbizXmlPatterns.SCREEN_CALL.accepts(val)) return
                if (!isDuplicate(val, isDefinition)) return
                holder.registerProblem(
                        val,
                        message('inspection.screen.duplicate.display.descriptor'),
                        WARNING)
            }

        }
    }

}
