package fr.nereide.inspection.java

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiLiteralExpression
import fr.nereide.inspection.common.OfbizBaseInspection
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.pattern.OfbizJavaPatterns
import org.jetbrains.annotations.NotNull

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static fr.nereide.inspection.InspectionBundle.message

class EntityNotFoundInJavaInspection extends OfbizBaseInspection {
    @Override
    @NotNull
    PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            void visitLiteralExpression(PsiLiteralExpression exp) {
                if (!PluginActivator.getInstance(exp.project).isActive() ||
                        !OfbizJavaPatterns.ENTITY_CALL.accepts(exp)) return
                if (!(OfbizProjectHelper.getInstance(exp.project).entityOrViewExists(exp.value))) {
                    holder.registerProblem(exp,
                            message('inspection.entity.not.found.display.descriptor'),
                            WARNING,
                    )
                }
            }
        }
    }
}
