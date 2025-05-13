package fr.nereide.inspection.groovy

import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.pattern.OfbizGroovyPatterns
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.groovy.codeInspection.BaseInspection
import org.jetbrains.plugins.groovy.codeInspection.BaseInspectionVisitor
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static fr.nereide.inspection.InspectionBundle.message

class ServiceNotFoundInGroovyInspection extends BaseInspection {

    @Override
    protected BaseInspectionVisitor buildVisitor() {
        return new BaseInspectionVisitor() {
            @Override
            void visitLiteralExpression(@NotNull GrLiteral element) {
                if (!PluginActivator.getInstance(element.project).isActive() ||
                        !(OfbizGroovyPatterns.SERVICE_CALL.accepts(element))) return
                OfbizProjectHelper ph = OfbizProjectHelper.getInstance(element.project)
                if (!(ph.getService(element.value))) {
                    this.registerError(
                            element,
                            message('inspection.service.not.found.display.descriptor'),
                            null,
                            WARNING)
                }
            }
        }
    }
}
