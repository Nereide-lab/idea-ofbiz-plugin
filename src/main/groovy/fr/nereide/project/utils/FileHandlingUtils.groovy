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
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.xml.XmlAttribute

import java.util.stream.Collectors

class FileHandlingUtils {
    FileHandlingUtils() {}
    private static final Logger LOG = Logger.getInstance(FileHandlingUtils.class)


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
