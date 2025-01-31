package fr.nereide.inspection.quickfix.xml

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.xml.XmlAttribute
import fr.nereide.dom.file.UiLabelFile
import fr.nereide.editor.OfbizEditorBundle
import fr.nereide.editor.actions.GetUserChoiceFromList
import fr.nereide.inspection.InspectionBundle
import fr.nereide.project.ProjectServiceInterface
import org.jetbrains.annotations.NotNull

import static fr.nereide.project.utils.MiscUtils.getComponentName

class CreateMissingLabelFix implements LocalQuickFix {

    CreateMissingLabelFix() {}

    @Override
    String getName() {
        return InspectionBundle.message('inspection.label.not.found.quickfix.create')
    }

    @Override
    String getFamilyName() {
        return getName()
    }

    @Override
    void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        ProjectServiceInterface ps = project.getService(ProjectServiceInterface.class)
        XmlAttribute attr = descriptor.getPsiElement() as XmlAttribute
        String labelName = attr.getValue()
        String[] choices = formatUiLabelFileListForDropDown(ps.getAllUiLabelFiles(project))
        String modalTitle = OfbizEditorBundle.message("editor.action.create.label.title")
        String modalText = OfbizEditorBundle.message("editor.action.create.label.file.select")
        GetUserChoiceFromList dial = new GetUserChoiceFromList(project, choices, modalTitle, modalText)
        if (!dial.showAndGet()) return
        if(dial.getComboBoxValue()) return
        createLabelInTargetFile(labelName, dial.getComboBoxValue())
        // On a le nom du fichier
    }

    static String[] formatUiLabelFileListForDropDown(List<UiLabelFile> uiLabelFileList) {
        return uiLabelFileList.collect { file ->
            "${file.xmlElement.containingFile.name} [${getComponentName(file)}]" as String
        }.toArray()
    }

    /*
    Est ce que juste le nom du fichier va suffire ?
    Non
     */
    void createLabelInTargetFile(String labelName, String fileName) {

    }
}
