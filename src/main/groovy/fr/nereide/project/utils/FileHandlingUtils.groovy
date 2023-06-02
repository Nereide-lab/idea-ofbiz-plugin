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

package fr.nereide.project.utils

import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlFile
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileElement
import com.intellij.util.xml.DomManager
import fr.nereide.dom.CompoundFile
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.worker.CompoundWorker
import fr.nereide.reference.common.ComponentAwareFileReferenceSet

import java.util.regex.Matcher
import java.util.stream.Collectors

class FileHandlingUtils {
    FileHandlingUtils() {}
    private static final Logger LOG = Logger.getInstance(FileHandlingUtils.class)

    /**
     * retrieves a specified DomElement from a psi file
     * The dom implementation of the file MUST exist.
     * @param file the file to search the element in
     * @param dm A DomManager
     * @param wantedElementName the name of the element to look for
     * @param fileType the DomFileElement of the file to search in
     * @param elementNameGetter the name of the method to get a particuliar element
     * @param listGetterMethod the name of the method that gets all the elements
     * @return
     */
    static PsiElement getElementFromSpecificFile(PsiFile file, DomManager dm, String wantedElementName, Class fileType,
                                                 String elementNameGetter, String listGetterMethod) {
        if (file instanceof XmlFile) {
            DomFileElement domFile = dm.getFileElement(file, fileType)
            boolean isCpd = false
            if (!domFile) {
                domFile = dm.getFileElement(file, CompoundFile.class)
                if (!domFile) return null
                isCpd = true
            }
            // TODO : There is certainly a better way to do that..
            List<DomElement> elementsInFile = isCpd ? CompoundWorker.getDomElementListFromCompound(domFile, listGetterMethod, fileType)
                    : domFile.getRootElement()."$listGetterMethod"()
            for (DomElement element : elementsInFile) {
                if (element."$elementNameGetter"().getValue().equalsIgnoreCase(wantedElementName)) {
                    return element.getXmlElement()
                }
            }
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
            List<String> pathPieces = splitPathToList(componentPathToFile)

            PsiDirectory currentDir = structureService.getComponentDir(pathPieces.first())
            try {
                for (int i = 1; i < pathPieces.size() - 1; i++) {
                    currentDir = currentDir.findSubdirectory(pathPieces.get(i))
                }
            } catch (NullPointerException ignored) {
                return null
            }
            PsiFile file = currentDir.findFile(pathPieces.last())
            return file
        }
        return null
    }

    /**
     * Splits a string like "component://order/path/to/file.xml"
     * @param componentPathToFile
     * @return a list with ['order', 'path', 'to', 'file.xml']
     */
    static List<String> splitPathToList(String componentPathToFile) {
        return Arrays.asList(componentPathToFile.split("\\s*/\\s*")).stream()
                .filter { !it.equalsIgnoreCase("") && !it.equalsIgnoreCase("component:") }
                .collect(Collectors.toList())
    }

}
