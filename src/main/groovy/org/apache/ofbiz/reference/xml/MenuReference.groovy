package org.apache.ofbiz.reference.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import com.intellij.util.xml.DomElement
import org.apache.ofbiz.project.ProjectServiceInterface
import org.jetbrains.annotations.Nullable

import java.util.regex.Matcher
import java.util.regex.Pattern

class MenuReference extends PsiReferenceBase<XmlElement> {
    MenuReference(XmlAttributeValue menuName, boolean soft) {
        super(menuName, soft)
    }

    @Nullable
    PsiElement resolve() {
        ProjectServiceInterface structureService = this.getElement().getProject().getService(ProjectServiceInterface.class)
        DomElement definition = structureService.getMenu(this.getValue())
        return definition != null ? definition.getXmlElement() : null
    }
}