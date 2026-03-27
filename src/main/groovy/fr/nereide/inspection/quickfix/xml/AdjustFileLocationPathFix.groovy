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

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileSystemItem
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import fr.nereide.inspection.InspectionBundle
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.utils.FileHandlingUtils
import org.apache.commons.text.similarity.LevenshteinDistance
import org.jetbrains.annotations.NotNull

/**
 * QuickFix class that attempts to fix a file location to the most probable one.
 * Uses the apache implementation of LevenshteinDistance
 */
class AdjustFileLocationPathFix implements LocalQuickFix {

    final static int DISTANCE_THRESHOLD = 15
    LevenshteinDistance distance

    AdjustFileLocationPathFix() {
        this.distance = new LevenshteinDistance(DISTANCE_THRESHOLD)
    }

    @Override
    String getName() {
        return InspectionBundle.message('inspection.location.target.file.not.found.use.quickfix.fixpath')
    }

    @Override
    String getFamilyName() {
        return name
    }

    @Override
    void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        XmlAttribute attr = descriptor.psiElement as XmlAttribute
        XmlAttributeValue val = attr.valueElement
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(descriptor.psiElement.project)
        List<String> pathList = FileHandlingUtils.splitPathToList(val.value)
        StringBuilder correctionAttempt = new StringBuilder().append('component://')

        String componentNameInput = pathList.first()
        String componentName
        PsiDirectory currentDir = ph.getComponentDir(componentNameInput)

        if (currentDir) {
            componentName = componentNameInput
        } else {
            List<String> allComponents = ph.collectAllComponentsNames()
            componentName = getMostLikelyCandidateStringInList(allComponents, componentNameInput)
            currentDir = ph.getComponentDir(componentName)
        }
        correctionAttempt.append(componentName).append('/')
        boolean doFix = true
        for (int i = 1; i < pathList.size(); i++) {
            String inputPieceName = pathList[i]
            if (inputPieceName.contains('.')) {
                List<PsiFile> files = currentDir.files
                String mostProbableFileName = getMostLikelyCandidateStringInDirOrFileList(files, inputPieceName)
                if (mostProbableFileName) {
                    correctionAttempt.append(mostProbableFileName)
                } else {
                    doFix = false
                    break
                }
            } else {
                List<PsiDirectory> possibleDirs = currentDir.subdirectories
                String mostProbableDirName = getMostLikelyCandidateStringInDirOrFileList(possibleDirs, inputPieceName)
                currentDir = currentDir.subdirectories.find { dir -> dir.name == mostProbableDirName }
                if (currentDir) {
                    correctionAttempt.append("${currentDir.name}/")
                } else {
                    doFix = false
                    break
                }
            }
        }
        if (doFix) {
            attr.value = correctionAttempt.toString()
        }
    }

    String getMostLikelyCandidateStringInDirOrFileList(List<PsiFileSystemItem> possibleDirs, String inputPieceName) {
        return getMostLikelyCandidateStringInList(possibleDirs*.name, inputPieceName)
    }

    String getMostLikelyCandidateStringInList(List<String> fileNames, String candidat) {
        return fileNames.findAll { fileName -> distance.apply(candidat, fileName) >= 0 }
                .min { fileName -> distance.apply(candidat, fileName) }
    }

}
