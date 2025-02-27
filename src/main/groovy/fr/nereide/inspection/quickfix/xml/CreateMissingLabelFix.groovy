package fr.nereide.inspection.quickfix.xml

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import fr.nereide.dom.file.UiLabelFile
import fr.nereide.editor.actions.OfbizDialogBuilder
import fr.nereide.editor.actions.OfbizSimpleListDialog
import fr.nereide.inspection.InspectionBundle
import fr.nereide.project.ProjectServiceInterface
import org.jetbrains.annotations.NotNull

import static com.intellij.openapi.application.ApplicationManager.getApplication
import static fr.nereide.editor.OfbizEditorBundle.message
import static fr.nereide.project.utils.MiscUtils.getComponentName
import static fr.nereide.project.utils.MiscUtils.getUiLabelSafeValue

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
        String componentName = getComponentName(attr)
        Map files = getLabelFilesList(ps, componentName)
        OfbizSimpleListDialog dial = new OfbizDialogBuilder(project)
                .from(files)
                .title(message("editor.action.create.label.title"))
                .text(message("editor.action.create.label.file.select"))
                .get()

        UiLabelFile fileToAddLabelIn

        if (getApplication().isUnitTestMode()) {
            fileToAddLabelIn = ps.getAllUiLabelFilesInComponent(componentName).first()
        } else if (!(dial.showAndGet()) || (!dial.getComboBoxValueOrKey())) {
            return
        } else {
            fileToAddLabelIn = dial.getComboBoxValueOrKey() as UiLabelFile
        }
        if (!fileToAddLabelIn) return
        createLabelInTargetFile(labelName, fileToAddLabelIn)
        fileToAddLabelIn.xmlElement.containingFile.navigate(true)
    }

    /**
     * Returns a map of label file names and associated pointers to files.
     * @param uiLabelFileList
     * @return
     */
    static Map<String, Object> formatUiLabelFileListForDropDown(List<UiLabelFile> uiLabelFileList) {
        Map<String, Object> result = [:]
        uiLabelFileList.forEach { file ->
            String componentName = getComponentName(file)
            String fileName = file.xmlElement.containingFile.name
            result << [("$fileName [$componentName]" as String): file]
        }
        return result
    }

    static void createLabelInTargetFile(String labelName, UiLabelFile labelFile) {
        XmlTag rootTag = labelFile.getXmlElement() as XmlTag

        // create new Prop tag
        XmlTag newPropertyTag = rootTag.createChildTag('property', '', '', false)
        newPropertyTag.setAttribute('key', getUiLabelSafeValue(labelName))

        // Create base value for english lang
        XmlTag basePropertyValue = newPropertyTag.createChildTag('value', '', 'New Label', false)
        basePropertyValue.setAttribute('xml:lang', 'en')
        newPropertyTag.addSubTag(basePropertyValue, false)

        rootTag.addSubTag(newPropertyTag, false)
    }
}
