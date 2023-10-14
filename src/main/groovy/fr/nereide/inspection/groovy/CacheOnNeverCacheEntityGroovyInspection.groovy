package fr.nereide.inspection.groovy

import com.intellij.codeInspection.ProblemsHolder
import fr.nereide.inspection.common.InspectionUtil
import fr.nereide.inspection.quickfix.RemoveCacheCallFix
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.groovy.codeInspection.GroovyLocalInspectionTool
import org.jetbrains.plugins.groovy.lang.psi.GroovyElementVisitor
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression

class CacheOnNeverCacheEntityGroovyInspection extends GroovyLocalInspectionTool {

    private final RemoveCacheCallFix myQuickFix = new RemoveCacheCallFix()

    @Override
    boolean isEnabledByDefault() {
        return true
    }

    @Override
    @NotNull
    GroovyElementVisitor buildGroovyVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new GroovyElementVisitor() {
            @Override
            void visitReferenceExpression(GrReferenceExpression exp) {
                InspectionUtil.checkAndRegisterCacheOnNeverCacheEntity(exp, holder, myQuickFix)
            }
        }
    }
}
