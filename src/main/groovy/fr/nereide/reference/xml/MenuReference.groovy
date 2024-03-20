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

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag
import fr.nereide.dom.FormFile
import fr.nereide.project.utils.XmlUtils

class MenuReference extends GenericXmlReference {

    Class fileType = FormFile.class

    MenuReference(XmlAttributeValue menuName, boolean soft) {
        super(menuName, soft)
    }

    PsiElement resolve() {
        XmlTag containingTag = (XmlTag) XmlUtils.getParentTag(this.getElement())
        if (!containingTag) {
            return null
        }
        PsiElement locationAttribute = containingTag.getAttribute('location')
        if (locationAttribute) {
            String locationAttributeValue = locationAttribute.getValue()
            return ps.getMenuFromFileAtLocation(dm, locationAttributeValue, this.getValue()).getXmlElement()
        } else if (XmlUtils.isInRightFile(this.getElement(), fileType, dm)) {
            PsiFile currentFile = this.getElement().getContainingFile()
            return ps.getMenuFromPsiFile(dm, currentFile, this.getElement().getValue()).getXmlElement()
        }
        return null
    }
}
