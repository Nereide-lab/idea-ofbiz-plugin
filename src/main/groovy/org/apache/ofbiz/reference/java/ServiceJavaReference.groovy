package org.apache.ofbiz.reference.java

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiReferenceBase
import com.intellij.util.xml.DomElement
import org.apache.ofbiz.project.ProjectServiceInterface
import org.jetbrains.annotations.Nullable

class ServiceJavaReference extends PsiReferenceBase<PsiLiteralExpression> {
    ServiceJavaReference(PsiLiteralExpression element, boolean soft) {
        super(element, soft)
    }

    @Nullable
    PsiElement resolve() {
        ProjectServiceInterface structureService = this.getElement().getProject().getService(ProjectServiceInterface.class)
        DomElement definition = structureService.getService(this.getValue())
        return definition != null ? definition.getXmlElement() : null
    }
}
