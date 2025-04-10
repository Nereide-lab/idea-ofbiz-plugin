package fr.nereide.inspection.groovy

import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.pattern.OfbizGroovyPatterns
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.groovy.codeInspection.BaseInspection
import org.jetbrains.plugins.groovy.codeInspection.BaseInspectionVisitor
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static fr.nereide.inspection.InspectionBundle.message

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
                if (!(OfbizGroovyPatterns.ENTITY_CALL.accepts(element))) return
                ProjectServiceInterface ps = element.getProject().getService(ProjectServiceInterface.class)
                if (ps.getEntities(element.value).size() > 1 || ps.getViewEntities(element.value).size() > 1) {
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
