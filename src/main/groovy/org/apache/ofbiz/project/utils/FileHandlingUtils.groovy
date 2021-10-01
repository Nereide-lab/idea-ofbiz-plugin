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

package org.apache.ofbiz.project.utils

import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlFile
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileElement
import com.intellij.util.xml.DomManager
import org.apache.ofbiz.project.ProjectServiceInterface
import org.apache.ofbiz.reference.common.ComponentAwareFileReferenceSet

import java.util.regex.Matcher
import java.util.stream.Collectors

class FileHandlingUtils {
    FileHandlingUtils() {}
    private static final Logger LOG = Logger.getInstance(FileHandlingUtils.class)

    /**
     * retrieves a specified DomElemeent from a psi file
     * The dom implementation of the file MUST exist.
     * @param file the file to search the element in
     * @param dm A DomManager
     * @param elementName the name of the element to look for
     * @param FILE_TYPE the DomFileElement of the file to search in
     * @param elementGetterMethod the name of the method to get a particuliar element
     * @param listGetterMethod the name of the method that gets all the elements
     * @return
     */
    static PsiElement getElementFromSpecificFile(PsiFile file, DomManager dm, String elementName, Class FILE_TYPE,
                                                 String elementGetterMethod, String listGetterMethod) {
        if (file instanceof XmlFile) {
            DomFileElement domFile = dm.getFileElement(file, FILE_TYPE)
            List<DomElement> elementsInFile = domFile.getRootElement()."$listGetterMethod"()
            for (DomElement element : elementsInFile) {
                if (element."$elementGetterMethod"().getValue().equalsIgnoreCase(elementName)) {
                    return element.getXmlElement()
                }
            }
        } else {
            LOG.warn("File was not of right type or something happend with file : " + file.getContainingDirectory())
        }
        return null
    }

    /**
     * return the file targeted by string of type "component://..."
     * @param componentPathToFile
     * @param structureService
     * @return PsiFile if foud or null
     */
    static PsiFile getTargetFile(String componentPathToFile, ProjectServiceInterface structureService) {
        Matcher componentMatcher = ComponentAwareFileReferenceSet.COMPONENT_NAME_PATTERN.matcher(componentPathToFile)

        if (componentMatcher.find() && componentMatcher.groupCount() != 0) {
            List<String> pathPieces = Arrays.asList(
                    componentPathToFile.split("\\s*/\\s*")).stream()
                    .filter { !it.equalsIgnoreCase("") && !it.equalsIgnoreCase("component:") }
                    .collect(Collectors.toList())

            PsiDirectory currentDir = structureService.getComponentDir(pathPieces.first())
            for (int i = 1; i < pathPieces.size() - 1; i++) {
                currentDir = currentDir.findSubdirectory(pathPieces.get(i))
            }
            PsiFile file = currentDir.findFile(pathPieces.last())
            return file
        }
        return null
    }

}
