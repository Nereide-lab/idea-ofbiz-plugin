package org.apache.ofbiz.xml.reference

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.xml.DomElement
import org.apache.ofbiz.base.project.ProjectServiceInterface

import org.jetbrains.annotations.Nullable

public class ControllerReference extends PsiReferenceBase<XmlAttributeValue> {
    public ControllerReference(XmlAttributeValue element, boolean soft) {
        super(element, soft);
    }

    @Nullable
    public PsiElement resolve() {
        ProjectServiceInterface structureService = this.element.getProject().getService(ProjectServiceInterface.class)
        DomElement definition = structureService.getControllerUri(this.getValue());
        return definition != null ? definition.getXmlElement() : null;
        return
    }
}