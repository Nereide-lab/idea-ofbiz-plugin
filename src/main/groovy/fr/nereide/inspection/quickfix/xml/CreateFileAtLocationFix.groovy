package fr.nereide.inspection.quickfix.xml

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlAttribute
import fr.nereide.inspection.InspectionBundle
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.utils.FileHandlingUtils
import fr.nereide.ui.dialog.FileCreateDialog
import org.jetbrains.annotations.NotNull

class CreateFileAtLocationFix implements LocalQuickFix {

    @Override
    String getName() {
        return InspectionBundle.message('inspection.location.target.file.not.found.use.quickfix.createfile')
    }

    @Override
    String getFamilyName() {
        return getName()
    }

    @Override
    void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        XmlAttribute attr = descriptor.getPsiElement() as XmlAttribute
        String val = attr.getValueElement().getValue()
        List<String> dirsInPath = FileHandlingUtils.splitPathToList(val)
        ProjectServiceInterface ps = project.getService(ProjectServiceInterface.class)
        PsiDirectory compoDir = ps.getComponentDir(dirsInPath.first())
        if (!compoDir) return

        PsiDirectory current = compoDir
        String fileName = dirsInPath.last()

        dirsInPath.remove(dirsInPath.first())
        dirsInPath.remove(dirsInPath.last())
        dirsInPath.each { String dir ->
            current.createSubdirectory(dir)
            current = current.findSubdirectory(dir)
        }

        // Appel d'une fenetre qui demande le type de fichier à créer

//        if (!new FileCreateDialog(project).showAndGet()) {
//            return
//        }

        // Si anulation, alors on créé pas
        PsiFile file = current.createFile(fileName)

        // Sinon, on affecte le template au fichier

        // Screen
        // Form
        // Menu
        // Fichier groovy
        // html template (ftl)
    }
}
