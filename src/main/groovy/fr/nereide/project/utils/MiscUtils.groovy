/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * 'License') you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * 'AS IS' BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package fr.nereide.project.utils

import static fr.nereide.project.pattern.OfbizPluginConstants.ENTITY_NAME_PATTERN

import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlFile
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileElement
import com.intellij.util.xml.DomManager
import fr.nereide.dom.file.ComponentFile
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral

import java.util.regex.Matcher

/**
 * Misc utility methods
 */
class MiscUtils {

    static String getComponentName(DomElement element) {
        return getComponentName(element.xmlElement)
    }

    static String getComponentName(PsiElement element) {
        DomManager dm = DomManager.getDomManager(element.project)
        PsiFile file = element.containingFile
        PsiDirectory dir = file?.originalFile?.parent
        if (!dir) return ''
        return findComponentName(dir, dm)
    }

    static String getComponentName(XmlElement element, Class myClassFile) {
        DomManager dm = DomManager.getDomManager(element.project)
        DomFileElement<DomElement> myFile = dm.getFileElement((element.containingFile as XmlFile), myClassFile)
        PsiDirectory dir = myFile?.originalFile?.parent
        if (!dir) return ''
        return findComponentName(dir, dm)
    }

    static String findComponentName(PsiDirectory dir, DomManager dm) {
        PsiDirectory dirToSearchIn = dir
        for (int i = 0; i < 10; i++) {
            XmlFile compoFile = dirToSearchIn.findFile('ofbiz-component.xml') as XmlFile
            if (compoFile) {
                return dm.getFileElement(compoFile, ComponentFile)?.rootElement?.name?.value
            }
            dirToSearchIn = dirToSearchIn.parent
            if (!dirToSearchIn) return ''
        }
        return ''
    }

    /**
     * Return safe uiLabelName
     */
    static String getUiLabelSafeValue(String text) {
        if (text.startsWith('${')) {
            return text.trim().substring(13, text.length() - 1)
        } else if (text.startsWith('{')) {
            return text.trim().substring(12, text.length() - 1)
        }
        return text
    }

    /**
     * return the text of a PsiElement without the surrounding quotes
     */
    static String getSafeTextValue(PsiElement originalElement) {
        return originalElement.text.replaceAll('"', '').replaceAll('\'', '')
    }

    /**
     * Tests the PsiElement parameter and returns true if the element is groovy language
     */
    static boolean isGroovy(PsiElement el) {
        return el.language.ID == 'Groovy'
    }

    static String getStringValueFromPsiElement(PsiElement element) {
        switch (element) {
            case XmlAttributeValue:
                return (element as XmlAttributeValue).value
            case PsiLiteralExpression:
                return (element as PsiLiteralExpression).value
            case GrLiteral:
                return (element as GrLiteral).value
            default:
                return element.text
        }
    }

    static boolean isInTestDir(DomFileElement domFile) {
        String dir = domFile.file.containingDirectory
        return dir.contains('/tests')
    }

    /**
     * Extracts entity name from declarations like
     * <code> EntityQuery.use(delegator).from() </code>
     * @param declaration the declaration string
     * @return the entity name
     */
    static String getEntityNameFromDeclarationString(String declaration) {
        Matcher matcher = ENTITY_NAME_PATTERN.matcher(declaration)
        String entityName = matcher.find() ? matcher.group(0) : null
        return entityName ? entityName.substring(1, entityName.length() - 1) : null
    }

}
