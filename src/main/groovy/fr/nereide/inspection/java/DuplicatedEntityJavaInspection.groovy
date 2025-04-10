package fr.nereide.inspection.java

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiLiteralExpression
import fr.nereide.inspection.common.OfbizBaseInspection
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.pattern.OfbizJavaPatterns
import org.jetbrains.annotations.NotNull

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static fr.nereide.inspection.InspectionBundle.message

class DuplicatedEntityJavaInspection extends OfbizBaseInspection {

    @Override
    @NotNull
    JavaElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            void visitLiteralExpression(@NotNull PsiLiteralExpression el) {
                if (!OfbizJavaPatterns.ENTITY_CALL) return
                ProjectServiceInterface ps = el.getProject().getService(ProjectServiceInterface.class)
                if (ps.getEntities(el.value).size() > 1 || ps.getViewEntities(el.value).size() > 1) {
                    holder.registerProblem(
                            el,
                            message('inspection.entity.duplicate.display.descriptor'),
                            WARNING)
                }
            }
        }
    }
}
