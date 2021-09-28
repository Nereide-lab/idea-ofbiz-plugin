package org.apache.ofbiz.reference.xml

import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlTag
import com.intellij.util.xml.DomElement
import org.apache.ofbiz.project.ProjectServiceInterface
import org.apache.ofbiz.reference.common.ComponentAwareFileReferenceSet

import java.util.regex.Matcher

class GenericXmlReference extends PsiReferenceBase<XmlAttributeValue> {
    String elementName

    GenericXmlReference(XmlAttributeValue element, boolean soft, String elementName) {
        super(element, soft)
        this.elementName = elementName
    }

    @Override
    PsiElement resolve() {
        ProjectServiceInterface structureService = this.getElement().getProject().getService(ProjectServiceInterface.class)
        // see if there is a location attribute
        // if there is, get file and look in file
        XmlTag parentTag = (XmlTag) getTag(element)
        if (parentTag) {
            PsiElement locationAttribute = parentTag.getAttribute('location')
            if (locationAttribute) {
                String locationAttributeValue = locationAttribute.getValue()
                PsiFile targetedFile = getTargetFile(locationAttributeValue, structureService)
                boolean troovz
            }
        }
        // if there insn't, look in PsiELement current file

        // Else, check everywhere

        DomElement definition = structureService.getScreen(elementName)
        return definition != null ? definition.getXmlElement() : null
    }
    
    private static PsiElement getTag(XmlElement xmlelement) {
        int i = 0;
        PsiElement parent = xmlelement.getParent()
        while (i < 5 && !(parent instanceof XmlTag)) {
            parent = parent.getParent()
        }
        return parent
    }

    private static PsiFile getTargetFile(String path, ProjectServiceInterface structureService) {
        Matcher componentMatcher = ComponentAwareFileReferenceSet.COMPONENT_NAME_PATTERN.matcher(path)
        if (componentMatcher.find() && componentMatcher.groupCount() != 0) {
            String componentName = componentMatcher.group(1)
            PsiDirectory compDir = structureService.getComponentDir(componentName)
            String fileName = Arrays.asList(path.split("\\s*/\\s*")).last()
            return compDir.findFile(fileName)
        }
        return null
    }
}
