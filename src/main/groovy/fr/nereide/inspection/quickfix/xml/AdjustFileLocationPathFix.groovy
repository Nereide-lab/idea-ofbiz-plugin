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
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.utils.FileHandlingUtils
import org.apache.commons.text.similarity.LevenshteinDistance
import org.jetbrains.annotations.NotNull

class AdjustFileLocationPathFix implements LocalQuickFix {

    final static int DISTANCE_THRESHOLD = 15
    LevenshteinDistance distance

    AdjustFileLocationPathFix() {
        this.distance = new LevenshteinDistance(DISTANCE_THRESHOLD)
    }

    @Override
    String getName() {
        return InspectionBundle.message('inspection.location.target.file.not.found.use.quickfix')
    }

    @Override
    String getFamilyName() {
        return getName()
    }

    @Override
    void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        XmlAttribute attr = descriptor.getPsiElement() as XmlAttribute
        XmlAttributeValue val = attr.getValueElement()
        ProjectServiceInterface ps = descriptor.getPsiElement().getProject().getService(ProjectServiceInterface.class)
        List<String> pathList = FileHandlingUtils.splitPathToList(val.value)
        StringBuilder correctionAttempt = new StringBuilder().append('component://')

        String componentNameInput = pathList.first()
        String componentName
        PsiDirectory currentDir = ps.getComponentDir(componentNameInput)

        if (!currentDir) {
            List<String> allComponents = ps.getAllComponentsNames()
            componentName = getMostLikelyCandidateStringInList(allComponents, componentNameInput)
            currentDir = ps.getComponentDir(componentName)
        } else {
            componentName = componentNameInput
        }
        correctionAttempt.append(componentName).append('/')
        boolean doFix = true
        for (int i = 1; i < pathList.size(); i++) {
            String inputPieceName = pathList[i]
            if (inputPieceName.contains('.')) {
                List<PsiFile> files = currentDir.getFiles()
                String mostProbableFileName = getMostLikelyCandidateStringInDirOrFileList(files, inputPieceName)
                if (mostProbableFileName) {
                    correctionAttempt.append(mostProbableFileName)
                } else {
                    doFix = false
                    break
                }
            } else {
                List<PsiDirectory> possibleDirs = currentDir.getSubdirectories()
                String mostProbableDirName = getMostLikelyCandidateStringInDirOrFileList(possibleDirs, inputPieceName)
                currentDir = currentDir.getSubdirectories().find { it.name == mostProbableDirName }
                if (currentDir) {
                    correctionAttempt.append(currentDir.name).append('/')
                } else {
                    doFix = false
                    break
                }
            }
        }
        if (doFix) {
            attr.setValue(correctionAttempt.toString())
        }
    }

    String getMostLikelyCandidateStringInDirOrFileList(List<PsiFileSystemItem> possibleDirs, String inputPieceName) {
        return getMostLikelyCandidateStringInList(possibleDirs.collect { it.getName() }, inputPieceName)
    }

    String getMostLikelyCandidateStringInList(List<String> list, String candidat) {
        return list.findAll { distance.apply(candidat, it) >= 0 }
                .min { distance.apply(candidat, it) }
    }
}
