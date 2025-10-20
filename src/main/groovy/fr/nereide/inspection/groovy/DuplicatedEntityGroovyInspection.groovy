package fr.nereide.inspection.groovy

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static fr.nereide.inspection.InspectionBundle.message

import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.pattern.OfbizGroovyPatterns
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.groovy.codeInspection.BaseInspection
import org.jetbrains.plugins.groovy.codeInspection.BaseInspectionVisitor
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral

/**
 * Groovy inspection
 */
class DuplicatedEntityGroovyInspection extends BaseInspection {

    @Override
    boolean isEnabledByDefault() {
        return true
    }

    @Override
    protected BaseInspectionVisitor buildVisitor() {
        return new BaseInspectionVisitor() {

            @Override
            void visitLiteralExpression(@NotNull GrLiteral element) {
                if (PluginActivator.getInstance(element.project).inactive) return
                if (!(OfbizGroovyPatterns.ENTITY_CALL.accepts(element))) return
                OfbizProjectHelper ph = OfbizProjectHelper.getInstance(element.project)
                if (ph.getEntities(element.value).size() > 1 || ph.getViewEntities(element.value).size() > 1) {
                    this.registerError(
                            element,
                            message('inspection.entity.duplicate.display.descriptor'),
                            null,
                            WARNING)
                }
            }

        }
    }

}
