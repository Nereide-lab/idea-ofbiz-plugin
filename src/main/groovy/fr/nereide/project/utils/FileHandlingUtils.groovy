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

import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.lang.xml.XMLLanguage
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.xml.XmlAttribute
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
     * @return PsiFile if found or null
     */
    static PsiFile getTargetFile(String componentPathToFile, ProjectServiceInterface structureService) {
        if (!componentPathToFile) return null
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


    /**
     * Creates a file from a template depending on the file type in parameter
     * @param project
     * @param fileType
     * @param attr the XML attribute that is used to create the file from
     * @param fileName
     * @param targetDir
     * @return the created PsiFile
     */
    static PsiFile createFileFromTemplate(Project project, String fileType, XmlAttribute attr, String fileName,
                                          PsiDirectory targetDir) {
        PsiFile fileToAdd
        FileTemplate template
        Properties templateProperties = new Properties()
        FileTemplateManager ftm = FileTemplateManager.getInstance(project)
        switch (fileType) {
            case 'Screen':
                template = ftm.getCodeTemplate('ScreenFileTemplate.xml')
                String elName = getNameAttrIfAny(attr)
                if (elName) templateProperties.setProperty('SCREEN_NAME', elName)
                fileToAdd = makeXmlFileFromTemplateAndProps(project, fileName, templateProperties, template)
                targetDir.add(fileToAdd)
                break
            case 'Menu':
                template = ftm.getCodeTemplate('MenuFileTemplate.xml')
                String elName = getNameAttrIfAny(attr)
                if (elName) templateProperties.setProperty('MENU_NAME', elName)
                fileToAdd = makeXmlFileFromTemplateAndProps(project, fileName, templateProperties, template)
                targetDir.add(fileToAdd)
                break
            case 'Form':
                template = ftm.getCodeTemplate('FormFileTemplate.xml')
                String elName = getNameAttrIfAny(attr)
                if (elName) templateProperties.setProperty('FORM_NAME', elName)
                fileToAdd = makeXmlFileFromTemplateAndProps(project, fileName, templateProperties, template)
                targetDir.add(fileToAdd)
                break
            case 'Controller':
                template = ftm.getCodeTemplate('ControllerFileTemplate.xml')
                fileToAdd = makeXmlFileFromTemplateAndProps(project, fileName, templateProperties, template)
                targetDir.add(fileToAdd)
                break
            default:
                fileToAdd = targetDir.createFile(fileName)
                break
        }
        return fileToAdd
    }

    private static String getNameAttrIfAny(XmlAttribute attr) {
        XmlAttribute nameAttr = attr.getParent().getAttribute('name')
        return nameAttr?.value
    }

    private static PsiFile makeXmlFileFromTemplateAndProps(Project project, String fileName,
                                                           Properties templateProperties, FileTemplate template) {
        String templateText = templateProperties ? template.getText(templateProperties) : template.getText()
        return PsiFileFactory.getInstance(project).createFileFromText(fileName, XMLLanguage.INSTANCE, templateText)
    }
}
