package fr.nereide.inspection.java

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static fr.nereide.inspection.InspectionBundle.message

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiLiteralExpression
import fr.nereide.inspection.common.OfbizBaseInspection
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.pattern.OfbizJavaPatterns
import org.jetbrains.annotations.NotNull

/**
 * Basic inspection
 */
class ServiceNotFoundInJavaInspection extends OfbizBaseInspection {

    @Override
    @NotNull
    PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {

            @Override
            void visitLiteralExpression(PsiLiteralExpression exp) {
                if (PluginActivator.getInstance(exp.project).inactive ||
                        !OfbizJavaPatterns.SERVICE_CALL.accepts(exp)) return
                if (!(OfbizProjectHelper.getInstance(exp.project).getService(exp.value as String))) {
                    holder.registerProblem(exp,
                            message('inspection.service.not.found.display.descriptor'),
                            WARNING,
                    )
                }
            }

        }
    }

}
