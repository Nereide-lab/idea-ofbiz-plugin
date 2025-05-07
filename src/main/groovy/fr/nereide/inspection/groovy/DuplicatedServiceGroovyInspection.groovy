package fr.nereide.inspection.groovy

import fr.nereide.project.PluginActivator
import fr.nereide.project.pattern.OfbizGroovyPatterns
import fr.nereide.reference.common.ServiceReference
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.groovy.codeInspection.BaseInspection
import org.jetbrains.plugins.groovy.codeInspection.BaseInspectionVisitor
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static fr.nereide.inspection.InspectionBundle.message

class DuplicatedServiceGroovyInspection extends BaseInspection {
    @Override
    boolean isEnabledByDefault() {
        return true
    }


    @Override
    protected BaseInspectionVisitor buildVisitor() {
        return new BaseInspectionVisitor() {
            @Override
            void visitLiteralExpression(@NotNull GrLiteral element) {
                if (!PluginActivator.getInstance(element.project).isActive()) return
                if (!(OfbizGroovyPatterns.SERVICE_CALL.accepts(element))) return
                if (!(new ServiceReference(element).multiResolve(false)?.size() > 1)) return
                this.registerError(
                        element,
                        message('inspection.service.duplicate.display.descriptor'),
                        null,
                        WARNING)
            }
        }
    }
}
