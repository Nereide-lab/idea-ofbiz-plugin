package org.apache.ofbiz.reference.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.xml.DomElement
import org.apache.ofbiz.project.ProjectServiceInterface
import org.jetbrains.annotations.Nullable

class UiLabelReference extends PsiReferenceBase<XmlAttributeValue> {
    UiLabelReference(XmlAttributeValue element, boolean soft) {
        super(element, soft)
    }

    @Nullable
    PsiElement resolve() {
        ProjectServiceInterface structureService = this.getElement().getProject().getService(ProjectServiceInterface.class)
        DomElement definition = structureService.getProperty(this.getUiLabelAwareValue())
        return definition != null ? definition.getXmlElement() : null
    }

    String getUiLabelAwareValue() {
        String text = this.getValue()
        if (text.startsWith('${')) {
            return text.substring(13, text.length() - 1)
        }
        return text
    }
}
