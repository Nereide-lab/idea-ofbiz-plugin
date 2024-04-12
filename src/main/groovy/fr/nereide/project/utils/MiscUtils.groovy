/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package fr.nereide.project.utils

import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlFile
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileElement
import com.intellij.util.xml.DomManager
import fr.nereide.dom.ComponentFile

class MiscUtils {

    /**
     * returns the name of the component dir containing the element
     * @param element
     * @return
     */
    static String getComponentName(DomElement element) {
        DomManager dm = DomManager.getDomManager(element.getXmlElement().getProject())
        PsiFile file = element.getXmlElement().getContainingFile()
        PsiDirectory dir = file.originalFile.getParent()
        try {
            return findComponentName(dir, dm)
        } catch (NullPointerException ignored) {
            return null
        }
    }

    static String getComponentName(PsiElement element) {
        DomManager dm = DomManager.getDomManager(element.getProject())
        PsiFile file = element.getContainingFile()
        PsiDirectory dir = file.originalFile.getParent()
        try {
            return findComponentName(dir, dm)
        } catch (NullPointerException ignored) {
            return null
        }
    }

    static String getComponentName(XmlElement element, Class myClassFile) {
        DomManager dm = DomManager.getDomManager(element.getProject())
        DomFileElement<DomElement> myFile = dm.getFileElement((element.getContainingFile() as XmlFile), myClassFile)
        PsiDirectory dir = myFile.originalFile.parent
        try {
            return findComponentName(dir, dm)
        } catch (NullPointerException ignored) {
            return null
        }
    }

    private static String findComponentName(PsiDirectory dir, DomManager dm) {
        for (def i = 0; i < 10; i++) {
            XmlFile compoFile = dir.findFile('ofbiz-component.xml') as XmlFile
            if (!compoFile) {
                dir = dir.getParent()
            } else {
                return dm.getFileElement(compoFile, ComponentFile.class)?.getRootElement()?.getName()?.getValue()
            }
        }
        return null
    }

    /**
     * Return safe uiLabelName
     * @return
     */
    static String getUiLabelSafeValue(String text) {
        if (text.startsWith('${')) {
            return text.substring(13, text.length() - 1)
        }
        return text
    }

    /**
     * return the textx of a PsiElement without the surrouding quotes
     * @param originalElement
     * @return
     */
    static String getSafeTextValue(PsiElement originalElement) {
        originalElement.getText().replaceAll('"', '').replaceAll('\'', '')
    }

    /**
     * Tests the PsiElement parameter and returns true if the element is groovy language
     * @param el
     * @return
     */
    static boolean isGroovy(PsiElement el) {
        return el.language.ID == 'Groovy'
    }
}
