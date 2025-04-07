package fr.nereide.inspection.java

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiLiteralExpression
import fr.nereide.project.pattern.OfbizJavaPatterns
import fr.nereide.reference.common.ServiceReference
import org.jetbrains.annotations.NotNull

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static fr.nereide.inspection.InspectionBundle.message

class DuplicatedServiceJavaInspection extends LocalInspectionTool {

    @Override
    boolean isEnabledByDefault() {
        return true
    }

    @Override
    @NotNull
    JavaElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            void visitLiteralExpression(@NotNull PsiLiteralExpression el) {
                if (!OfbizJavaPatterns.SERVICE_CALL) return
                if (!(new ServiceReference(el).multiResolve(false)?.size() > 1)) return
                holder.registerProblem(
                        el,
                        message('inspection.service.duplicate.display.descriptor'),
                        WARNING)
            }
        }
    }
}
