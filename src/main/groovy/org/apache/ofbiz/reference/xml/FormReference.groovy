package org.apache.ofbiz.reference.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import com.intellij.util.xml.DomElement
import org.apache.ofbiz.project.ProjectServiceInterface
import org.jetbrains.annotations.Nullable

class FormReference extends PsiReferenceBase<XmlElement> {
    FormReference(XmlAttributeValue formName, boolean soft) {
        super(formName, soft)
    }

    @Nullable
    PsiElement resolve() {
        ProjectServiceInterface structureService = this.getElement().getProject().getService(ProjectServiceInterface.class)

        DomElement definition = structureService.getForm(this.getValue())
        // Si on ne trouve pas dans les forms, on cherche dans les grids
        if (!definition) definition = structureService.getGrid(this.getValue())
        return definition != null ? definition.getXmlElement() : null
    }
}