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
import java.util.stream.Collectors

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
                PsiFile targetedFile = getTargetFile(locationAttributeValue, structureService) // on a le fichier

                //regarder dans le fichier si ont trouve l'élément.
                // Pour ça il nous faut le type de l'élément
                //DomElement test = ReferenceUtils.findInFile( targetedFile, this.getElement())

            }
        }
        // if there insn't, look in PsiELement current file

        // Else, check everywhere

        DomElement definition = structureService.getScreen(elementName)
        return definition != null ? definition.getXmlElement() : null
    }

    /**
     * returns the xml tag parent of xml element or null if not found
     * @param xmlelement
     * @return
     */
    private static PsiElement getTag(XmlElement xmlelement) {
        int i = 0;
        PsiElement parent = xmlelement.getParent()
        while (i < 5 && !(parent instanceof XmlTag)) {
            parent = parent.getParent()
        }
        return parent instanceof XmlTag ? parent : null
    }

    /**
     * return the file targeted by string of type "component://..."
     * @param componentPathToFile
     * @param structureService
     * @return PsiFile if foud or null
     */
    private static PsiFile getTargetFile(String componentPathToFile, ProjectServiceInterface structureService) {
        Matcher componentMatcher = ComponentAwareFileReferenceSet.COMPONENT_NAME_PATTERN.matcher(componentPathToFile)
        if (componentMatcher.find() && componentMatcher.groupCount() != 0) {
            List<String> pathPieces = Arrays.asList(
                componentPathToFile.split("\\s*/\\s*")).stream()
                    .filter{ !it.equalsIgnoreCase("") && !it.equalsIgnoreCase("component:") }
                    .collect(Collectors.toList())
            PsiDirectory currentDir = structureService.getComponentDir(pathPieces.first())
            for(int i = 1; i < pathPieces.size()-1; i++){
                currentDir = currentDir.findSubdirectory(pathPieces.get(i))
            }
            PsiFile file = currentDir.findFile(pathPieces.last())
            return file
        }
        return null
    }
}
