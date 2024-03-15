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
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.intellij.util.xml.DomManager
import fr.nereide.project.ProjectServiceInterface

abstract class GenericXmlReference extends PsiReferenceBase<XmlAttributeValue> {

    DomManager dm
    ProjectServiceInterface ps

    GenericXmlReference(XmlAttributeValue element, boolean soft) {
        super(element, soft)
        dm = DomManager.getDomManager(element.getProject())
        ps = element.getProject().getService(ProjectServiceInterface.class)
    }

    /**
     * returns the xml tag parent of xml element or null if not found
     * @param xmlelement
     * @return
     */
    protected static PsiElement getTag(XmlElement xmlelement) {
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
     * @param fileType
     * @param dm
     * @return
     */
    static boolean isInRightFile(XmlAttributeValue value, Class fileType, DomManager dm) {
        PsiFile currentFile = value.getContainingFile() as XmlFile
        return dm.getFileElement(currentFile, fileType) != null
    }

}
