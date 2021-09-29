package org.apache.ofbiz.reference.xml

import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileElement
import com.intellij.util.xml.DomManager
import org.apache.ofbiz.dom.ScreenFile
import org.apache.ofbiz.project.ProjectServiceInterface
import org.apache.ofbiz.project.utils.FileHandlingUtils
import org.apache.ofbiz.reference.common.ComponentAwareFileReferenceSet

import java.util.regex.Matcher
import java.util.stream.Collectors

class GenericXmlReference extends PsiReferenceBase<XmlAttributeValue> {
    private static final Logger LOG = Logger.getInstance(GenericXmlReference.class)
    String elementName
    final String LIST_GETTER_METHOD
    final String ELEMENT_GETTER_METHOD
    final Class FILE_TYPE

    GenericXmlReference(XmlAttributeValue element, boolean soft, String elementName, String LIST_GETTER_METHOD,
                                                                                     String ELEMENT_GETTER_METHOD,
                                                                                     Class FILE_TYPE) {
        super(element, soft)
        this.elementName = elementName
        this.ELEMENT_GETTER_METHOD = ELEMENT_GETTER_METHOD
        this.LIST_GETTER_METHOD = LIST_GETTER_METHOD
        this.FILE_TYPE = FILE_TYPE
    }

    @Override
    PsiElement resolve() {
        ProjectServiceInterface structureService = this.getElement().getProject().getService(ProjectServiceInterface.class)
        DomManager dm = DomManager.getDomManager(this.getElement().getProject())

        XmlTag parentTag = (XmlTag) getTag(element)
        if (parentTag) {
            PsiElement locationAttribute = parentTag.getAttribute('location')
            if (locationAttribute) {
                String locationAttributeValue = locationAttribute.getValue()
                PsiFile targetedFile = FileHandlingUtils.getTargetFile(locationAttributeValue, structureService) // on a le fichier
                return FileHandlingUtils.getElementFromSpecificFile(targetedFile, dm, this.elementName, this.FILE_TYPE,
                                                this.ELEMENT_GETTER_METHOD, this.LIST_GETTER_METHOD)
            } else {
                // On regarde dans le fichier courant
                PsiFile currentFile = this.getElement().getContainingFile()
                return FileHandlingUtils.getElementFromSpecificFile(currentFile, dm, this.elementName, this.FILE_TYPE,
                        this.ELEMENT_GETTER_METHOD, this.LIST_GETTER_METHOD)
            }
        } else {
            // Si pas de tag xml trouvé ou problème, on utilise la methode normale
            String elementGetterMethod = this.ELEMENT_GETTER_METHOD
            DomElement definition = structureService."$elementGetterMethod"(elementName)
            return definition != null ? definition.getXmlElement() : null
        }
    }

    /**
     * returns the xml tag parent of xml element or null if not found
     * @param xmlelement
     * @return
     */
    private static PsiElement getTag(XmlElement xmlelement) {
        int i = 0
        PsiElement parent = xmlelement.getParent()
        while (i < 5 && !(parent instanceof XmlTag)) {
            parent = parent.getParent()
        }
        return parent instanceof XmlTag ? parent : null
    }
}
