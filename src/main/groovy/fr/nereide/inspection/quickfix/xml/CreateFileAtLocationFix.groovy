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
