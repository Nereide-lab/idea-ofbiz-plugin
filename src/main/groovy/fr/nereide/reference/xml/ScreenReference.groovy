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

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag
import com.intellij.util.xml.DomManager
import fr.nereide.dom.ScreenFile
import fr.nereide.project.ProjectServiceInterface

import static fr.nereide.project.utils.XmlUtils.*

class ScreenReference extends GenericXmlReference {

    Class fileType = ScreenFile.class

    ScreenReference(XmlAttributeValue screenName, TextRange textRange, boolean soft) {
        super(screenName, textRange, soft)
    }

    ScreenReference(XmlAttributeValue screenName, boolean soft) {
        super(screenName, soft)
    }

    PsiElement resolve() {
        XmlTag containingTag = (XmlTag) getParentTag(this.getElement())
        if (!containingTag) {
            return null
        }
        PsiElement locationAttribute = containingTag.getAttribute('location')
        if (locationAttribute) {
            String locationAttributeValue = locationAttribute.getValue()
            return ps.getScreenFromFileAtLocation(dm, locationAttributeValue, this.getValue()).getXmlElement()
        } else if (isPageReferenceFromController(containingTag)) {
            return resolveScreenInController(this.getElement(), ps, dm)
        } else if (isInRightFile(this.getElement(), fileType, dm)) {
            PsiFile currentFile = this.getElement().getContainingFile()
            return ps.getScreenFromPsiFile(dm, currentFile, this.getElement().getValue()).getXmlElement()
        }
        return null
    }

    // TODO : passer par un RangeInElement
    static PsiElement resolveScreenInController(XmlAttributeValue element, ProjectServiceInterface ps, DomManager dm) {
        String screenName = getScreenNameFromControllerString(element)
        String controllerStringValue = element.getValue()
        String fileComponentLocation = controllerStringValue.substring(0, controllerStringValue.length() - screenName.length() - 1)
        return ps.getScreenFromFileAtLocation(dm, fileComponentLocation, screenName).getXmlElement()
    }

}