package fr.nereide.inspection.java

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiLiteralExpression
import fr.nereide.inspection.common.OfbizBaseInspection
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
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
                if (!PluginActivator.getInstance(el.project).isActive()) return
                if (!OfbizJavaPatterns.ENTITY_CALL) return
                OfbizProjectHelper ph = OfbizProjectHelper.getInstance(el.project)
                if (ph.getEntities(el.value).size() > 1 || ph.getViewEntities(el.value).size() > 1) {
                    holder.registerProblem(
                            el,
                            message('inspection.entity.duplicate.display.descriptor'),
                            WARNING)
                }
            }
        }
    }
}
