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

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.intellij.util.xml.DomManager

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Various xml utility methods
 */
class XmlUtils {

    /**
     * returns the xml tag parent of xml element or null if not found
     */
    static XmlTag getParentTag(XmlElement xmlElement) {
        if (!xmlElement) return null
        int i = 0
        PsiElement parent = xmlElement.parent
        while (i < 5 && parent && !(parent instanceof XmlTag)) {
            parent = parent.parent
        }
        return parent instanceof XmlTag ? parent : null
    }

    /**
     * Checks that the XmlElement we want reference on is in a file where it would be defined
     */
    static boolean isInRightFile(XmlAttributeValue value, Class fileType, DomManager dm) {
        PsiFile currentFile = value.containingFile as XmlFile
        return dm.getFileElement(currentFile, fileType) != null
    }

    static boolean isPageReferenceFromController(XmlTag containingTag) {
        return (containingTag.getAttribute('page') != null)
    }

    static String getScreenNameFromControllerString(XmlAttributeValue name) {
        Matcher matcher = Pattern.compile("[^#]*\$").matcher(name.value)
        return matcher.find() ? matcher.group(0) : null
    }

}
