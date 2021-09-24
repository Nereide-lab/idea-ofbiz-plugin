package org.apache.ofbiz.reference.groovy

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.util.xml.DomElement
import org.apache.ofbiz.project.ProjectServiceInterface
import org.jetbrains.annotations.Nullable
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral

class EntityGroovyReference extends PsiReferenceBase<GrLiteral> {
    EntityGroovyReference(GrLiteral element, boolean soft) {
        super(element, soft);
    }

    @Nullable
    PsiElement resolve() {
        ProjectServiceInterface structureService = this.getElement().getProject().getService(ProjectServiceInterface.class)
        DomElement definition = structureService.getEntity(this.getValue())
        return definition != null ? definition.getXmlElement() : null
    }
}
