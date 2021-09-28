package org.apache.ofbiz.reference.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import com.intellij.util.xml.DomElement
import org.apache.ofbiz.project.ProjectServiceInterface
import org.jetbrains.annotations.Nullable

import java.util.regex.Matcher
import java.util.regex.Pattern

class ScreenReference extends PsiReferenceBase<XmlElement> {
    //regex qui récupère tout ce qu'il y a derrière le '#', donc le nom de l'écran
    static final Pattern SCREEN_NAME_PATTERN = Pattern.compile("[^#]*\$")

    ScreenReference(XmlAttributeValue formName, boolean soft) {
        super(formName, soft)
    }

    @Nullable
    PsiElement resolve() {
        ProjectServiceInterface structureService = this.getElement().getProject().getService(ProjectServiceInterface.class)
        Matcher matcher = SCREEN_NAME_PATTERN.matcher(this.getValue())
        DomElement definition = structureService.getScreen(matcher.find() ? matcher.group(0) : this.getValue())
        return definition != null ? definition.getXmlElement() : null
    }
}