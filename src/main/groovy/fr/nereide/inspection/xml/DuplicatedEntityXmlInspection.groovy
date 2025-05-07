package fr.nereide.inspection.xml

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttributeValue
import fr.nereide.inspection.common.OfbizBaseInspection
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.pattern.OfbizXmlPatterns
import org.jetbrains.annotations.NotNull

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static fr.nereide.inspection.InspectionBundle.message

class DuplicatedEntityXmlInspection extends OfbizBaseInspection {

    @Override
    @NotNull
    PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {

        return new XmlElementVisitor() {
            @Override
            void visitXmlAttributeValue(@NotNull XmlAttributeValue val) {
                if (!(OfbizXmlPatterns.ENTITY_OR_VIEW_CALL.accepts(val))) return
                OfbizProjectHelper ph = OfbizProjectHelper.getInstance(val.project)
                if (ph.getEntities(val.value).size() > 1 || ph.getViewEntities(val.value).size() > 1) {
                    holder.registerProblem(
                            val,
                            message('inspection.entity.duplicate.display.descriptor'),
                            WARNING)
                }
            }
        }
    }
}
