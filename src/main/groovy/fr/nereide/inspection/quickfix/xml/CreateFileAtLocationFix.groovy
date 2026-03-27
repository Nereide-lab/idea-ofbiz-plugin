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
package fr.nereide.inspection.quickfix.xml

import static com.intellij.openapi.application.ApplicationManager.getApplication
import static fr.nereide.editor.OfbizEditorBundle.message

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlAttribute
import fr.nereide.editor.actions.OfbizDialogBuilder
import fr.nereide.editor.actions.OfbizSimpleListDialog
import fr.nereide.inspection.InspectionBundle
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.utils.FileHandlingUtils
import org.jetbrains.annotations.NotNull

/**
 * Quick-fix that attempts to create a file after a user dialog
 */
class CreateFileAtLocationFix implements LocalQuickFix {

    final String name = InspectionBundle.message('inspection.location.target.file.not.found.use.quickfix.createfile')

    final String familyName = name

    @Override
    void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        XmlAttribute attr = descriptor.psiElement as XmlAttribute
        String val = attr.valueElement.value
        List<String> dirsInPath = FileHandlingUtils.splitPathToList(val)
        PsiDirectory compoDir = OfbizProjectHelper.getInstance(project).getComponentDir(dirsInPath.first())
        if (!compoDir) return
        PsiDirectory current = compoDir
        String fileName = dirsInPath.last()

        dirsInPath.remove(dirsInPath.first())
        dirsInPath.remove(dirsInPath.last())

        dirsInPath.each { String dir ->
            if (!current.findSubdirectory(dir)) {
                current.createSubdirectory(dir)
            }
            current = current.findSubdirectory(dir)
        }

        if (fileName.split('\\.').last() == 'ftl') {
            current.createFile(fileName)
            return
        }

        PsiFile fileToAdd
        if (application.unitTestMode) {
            fileToAdd = FileHandlingUtils.generateFileFromTemplate(project, null, attr, fileName, current)
        } else {
            OfbizSimpleListDialog dial = new OfbizDialogBuilder(project)
                    .from(['Blank', 'Screen', 'Menu', 'Form', 'Controller'])
                    .title(message('editor.action.create.ofbiz.file.title'))
                    .text(message('editor.action.create.ofbiz.file.select'))
                    .get()
            if (!dial.showAndGet()) return
            String selected = dial.comboBoxValueOrKey
            fileToAdd = FileHandlingUtils.generateFileFromTemplate(project, selected, attr, fileName, current)
        }
        if (fileToAdd) fileToAdd.navigate(true)
    }

}
