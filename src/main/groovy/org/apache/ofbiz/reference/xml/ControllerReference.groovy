package org.apache.ofbiz.reference.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.xml.DomElement
import org.apache.ofbiz.project.ProjectServiceInterface
import org.jetbrains.annotations.Nullable

class ControllerReference extends PsiReferenceBase<XmlAttributeValue> {
    ControllerReference(XmlAttributeValue element, boolean soft) {
        super(element, soft)
    }

    @Nullable
    PsiElement resolve() {
        ProjectServiceInterface structureService = this.element.getProject().getService(ProjectServiceInterface.class)
        DomElement definition = structureService.getControllerUri(this.getValue())
        return definition != null ? definition.getXmlElement() : null
    }
}