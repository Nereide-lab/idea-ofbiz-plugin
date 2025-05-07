package fr.nereide.inspection.xml

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import fr.nereide.inspection.common.OfbizBaseInspection
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.pattern.OfbizXmlPatterns
import fr.nereide.reference.xml.ScreenReference
import org.jetbrains.annotations.NotNull

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static fr.nereide.inspection.InspectionBundle.message

class DuplicatedScreenInspection extends OfbizBaseInspection {

    @Override
    @NotNull
    PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {

        return new XmlElementVisitor() {
            @Override
            void visitXmlAttributeValue(@NotNull XmlAttributeValue val) {
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

    static boolean isDuplicate(XmlAttributeValue val, boolean isDef) {
        XmlElement valToUse = isDef ? val : new ScreenReference(val, true).resolve() as XmlElement
        if (!valToUse) return false
        return OfbizProjectHelper.getInstance(val.project)
                .getAllScreenFromCurrentFileFromElement(valToUse)
                .findAll { it.name.value == val.value }
                .size() > 1
    }

}
