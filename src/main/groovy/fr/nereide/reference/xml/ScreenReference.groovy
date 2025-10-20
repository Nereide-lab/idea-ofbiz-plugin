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

import static fr.nereide.project.pattern.OfbizPluginConstants.FILE_AND_ELEMENT_SEPARATOR

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag
import fr.nereide.dom.element.screen.Screen
import fr.nereide.dom.file.ScreenFile
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.utils.XmlUtils

/**
 * Part of the OFBiz plugin reference and navigation system
 */
class ScreenReference extends GenericXmlReference {

    Class fileType = ScreenFile

    ScreenReference(XmlAttributeValue screenName, TextRange textRange, boolean soft) {
        super(screenName, textRange, soft)
    }

    ScreenReference(XmlAttributeValue screenName, boolean soft) {
        super(screenName, soft)
    }

    ScreenReference(XmlAttributeValue screenName) {
        super(screenName, true)
    }

    PsiElement resolve() {
        XmlTag containingTag = (XmlTag) XmlUtils.getParentTag(this.element)
        if (!containingTag) {
            return null
        }
        PsiElement locationAttribute = containingTag.getAttribute('location')
        if (locationAttribute) {
            if (locationAttribute.value.contains('${')) {
                return null
            }
            Screen screen = ph.getScreenFromFileAtLocation(locationAttribute.value, this.value)
            return screen ? screen.xmlElement : null
        } else if (XmlUtils.isPageReferenceFromController(containingTag)) {
            return resolveScreenInController(this.element, ph)
        } else if (XmlUtils.isInRightFile(this.element, fileType, dm)) {
            PsiFile currentFile = this.element.containingFile
            Screen screen = ph.getScreenFromPsiFile(currentFile, this.element.value)
            return screen ? screen.xmlElement : null
        }
        return null
    }

    PsiElement resolveScreenInController(XmlAttributeValue element, OfbizProjectHelper ph) {
        String screenName = XmlUtils.getScreenNameFromControllerString(element)
        String controllerStringValue = element.value
        String fileComponentLocation = controllerStringValue.substring(0,
                controllerStringValue.length() - screenName.length() - 1)
        this.rangeInElement = new TextRange(controllerStringValue.indexOf(FILE_AND_ELEMENT_SEPARATOR),
                controllerStringValue.length())
        return ph.getScreenFromFileAtLocation(fileComponentLocation, screenName).xmlElement
    }

}
