/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License") you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package fr.nereide.reference.xml

import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomManager
import fr.nereide.dom.ScreenFile
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.utils.FileHandlingUtils

import java.util.regex.Matcher
import java.util.regex.Pattern

class GenericXmlReference extends PsiReferenceBase<XmlAttributeValue> {
    private static final Logger LOG = Logger.getInstance(GenericXmlReference.class)
    //String elementName
    final String ELEMENT_LIST_GETTER_METHOD
    final String ELEMENT_NAME_GETTER_METHOD
    final String ELEMENT_GETTER_METHOD
    final Class FILE_TYPE

    GenericXmlReference(XmlAttributeValue element, boolean soft, String ELEMENT_LIST_GETTER_METHOD,
                        String ELEMENT_NAME_GETTER_METHOD,
                        String ELEMENT_GETTER_METHOD,
                        Class FILE_TYPE) {
        super(element, soft)
        this.ELEMENT_NAME_GETTER_METHOD = ELEMENT_NAME_GETTER_METHOD
        this.ELEMENT_LIST_GETTER_METHOD = ELEMENT_LIST_GETTER_METHOD
        this.ELEMENT_GETTER_METHOD = ELEMENT_GETTER_METHOD
        this.FILE_TYPE = FILE_TYPE
        //this.getElement.getName() = setElementName(element)
    }

    @Override
    PsiElement resolve() {
        ProjectServiceInterface structureService = this.getElement().getProject().getService(ProjectServiceInterface.class)
        DomManager dm = DomManager.getDomManager(this.getElement().getProject())
        XmlTag containingTag = (XmlTag) getTag(this.getElement())
        if (containingTag) {
            PsiElement locationAttribute = containingTag.getAttribute('location')
            if (locationAttribute) {
                //On va chercher dans le fichier ciblé
                String locationAttributeValue = locationAttribute.getValue()
                PsiFile targetedFile = FileHandlingUtils.getTargetFile(locationAttributeValue, structureService)
                return FileHandlingUtils.getElementFromSpecificFile(targetedFile, dm, this.getElement().getValue(), this.FILE_TYPE,
                        this.ELEMENT_NAME_GETTER_METHOD, this.ELEMENT_LIST_GETTER_METHOD)
            } else if (isPageReferenceFromController(containingTag)) {
                return resolveScreenInController(this.getElement(), structureService, dm)
            } else if (isInRightFile(this.getElement(), this.FILE_TYPE, dm)) {
                // On regarde dans le fichier courant
                PsiFile currentFile = this.getElement().getContainingFile()
                return FileHandlingUtils.getElementFromSpecificFile(currentFile, dm, this.getElement().getValue(), this.FILE_TYPE,
                        this.ELEMENT_NAME_GETTER_METHOD, this.ELEMENT_LIST_GETTER_METHOD)
            } else {
                // Si on a pas de location, mais qu'on est pas dans un fichier pertinent => standard
                return resolveStandard(structureService, this.getElement())
            }
        } else {
            // Si pas de tag xml trouvé ou problème, on utilise la methode normale
            return resolveStandard(structureService, this.getElement())
        }
    }

    private XmlElement resolveStandard(ProjectServiceInterface structureService, def element) {
        String elementGetterMethod = this.ELEMENT_GETTER_METHOD
        DomElement definition = structureService."$elementGetterMethod"(element.getValue())
        return definition != null ? definition.getXmlElement() : null
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

    /**
     * Checks that the XmlElement we want reference on is in a file where it would be defined
     * @param value
     * @param FILE_TYPE
     * @param dm
     * @return
     */
    static boolean isInRightFile(XmlAttributeValue value, Class FILE_TYPE, DomManager dm) {
        PsiFile currentFile = value.getContainingFile() as XmlFile
        return dm.getFileElement(currentFile, FILE_TYPE) != null
    }

    static boolean isPageReferenceFromController(XmlTag containingTag) {
        return (containingTag.getAttribute('page') != null)
    }

    static PsiElement resolveScreenInController(XmlAttributeValue element, ProjectServiceInterface structureService, DomManager dm) {
        String screenName = getScreenName(element)
        String stringValue = element.getValue()
        String fileComponentLocation = stringValue.substring(0, stringValue.length() - screenName.length() - 1)
        PsiFile targetedFile = FileHandlingUtils.getTargetFile(fileComponentLocation, structureService)
        return FileHandlingUtils.getElementFromSpecificFile(targetedFile, dm, screenName, ScreenFile.class,
                "getName", "getScreens")
    }

    /**
     * For the case of screen name in controller
     * @param name
     * @return
     */
    private static String getScreenName(XmlAttributeValue name) {
        //regex that gets the screen name
        final Pattern SCREEN_NAME_PATTERN = Pattern.compile("[^#]*\$")
        Matcher matcher = SCREEN_NAME_PATTERN.matcher(name.getValue())
        return matcher.find() ? matcher.group(0) : null
    }
}
