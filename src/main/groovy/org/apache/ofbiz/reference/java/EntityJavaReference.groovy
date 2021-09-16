package org.apache.ofbiz.reference.java

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiReferenceBase
import com.intellij.util.xml.DomElement
import org.apache.ofbiz.project.ProjectServiceInterface
import org.jetbrains.annotations.Nullable

class EntityJavaReference extends PsiReferenceBase<PsiLiteralExpression> {
    EntityJavaReference(PsiLiteralExpression element, boolean soft) {
        super(element, soft)
    }

    @Nullable
    PsiElement resolve() {
        ProjectServiceInterface structureService = this.getElement().getProject().getService(ProjectServiceInterface.class)
        DomElement definition = structureService.getEntity(this.getValue())
        if(!definition){
            definition = structureService.getViewEntity(this.getValue())
        }
        return definition != null ? definition.getXmlElement() : null
    }
}
