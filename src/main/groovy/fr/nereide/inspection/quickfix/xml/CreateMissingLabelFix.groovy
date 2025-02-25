package fr.nereide.inspection.quickfix.xml

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.xml.XmlAttribute
import fr.nereide.dom.file.UiLabelFile
import fr.nereide.editor.actions.OfbizDialogBuilder
import fr.nereide.editor.actions.OfbizSimpleListDialog
import fr.nereide.inspection.InspectionBundle
import fr.nereide.project.ProjectServiceInterface
import org.jetbrains.annotations.NotNull

import static fr.nereide.editor.OfbizEditorBundle.*
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
        OfbizSimpleListDialog dial = new OfbizDialogBuilder(project)
                .from(formatUiLabelFileListForDropDown(ps.getAllUiLabelFiles(project)))
                .title(message("editor.action.create.label.title"))
                .text(message("editor.action.create.label.file.select"))
                .get()

        if (!dial.showAndGet()) return
        if (dial.getComboBoxValueOrKey()) return
        createLabelInTargetFile(labelName, dial.getComboBoxValueOrKey() as UiLabelFile)
    }

    static Map<String, Object> formatUiLabelFileListForDropDown(List<UiLabelFile> uiLabelFileList) {
        Map result = [:]
        uiLabelFileList.forEach { file ->
            String componentName = getComponentName(file)
            String fileName = file.xmlElement.containingFile.name
            result << [("$fileName [$componentName]" as String): file]
        }
        return result
    }

    void createLabelInTargetFile(String labelName, UiLabelFile fileName) {
        return
    }
}
