package fr.nereide.inspection.quickfix.xml

import static com.intellij.openapi.application.ApplicationManager.getApplication
import static fr.nereide.editor.OfbizEditorBundle.message
import static fr.nereide.project.utils.MiscUtils.getComponentName
import static fr.nereide.project.utils.MiscUtils.getUiLabelSafeValue

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import fr.nereide.dom.file.UiLabelFile
import fr.nereide.editor.actions.OfbizDialogBuilder
import fr.nereide.editor.actions.OfbizSimpleListDialog
import fr.nereide.inspection.InspectionBundle
import fr.nereide.project.OfbizProjectHelper
import org.jetbrains.annotations.NotNull

/**
 * Quickfix that creates a label in a property file when missing
 */
class CreateMissingLabelFix implements LocalQuickFix {

    final String name = InspectionBundle.message('inspection.label.not.found.quickfix.create')

    final String familyName = getName()

    /**
     * Returns a map of label file names and associated pointers to files.
     */
    static Map getLabelFilesList(OfbizProjectHelper ph, String componentName) {
        Map result = [:]
        ph.collectAllUiLabelFilesInComponent(componentName)
                .forEach { file -> result << addLabelFileToMap(file, true) }
        ph.collectAllUiLabelFiles() // codenarc-disable UnnecessaryGetter
                .forEach { file -> result << addLabelFileToMap(file, false) }
        return result
    }

    static Map addLabelFileToMap(UiLabelFile file, boolean isCurrentComponent) {
        String fileName = file.xmlElement.containingFile.name
        String choiceStr = "$fileName [component: ${isCurrentComponent ? 'CURRENT' : getComponentName(file)}]"
        return [(choiceStr):file]
    }

    static void addLabelInTargetFile(String labelName, UiLabelFile labelFile) {
        XmlTag rootTag = labelFile.xmlElement as XmlTag

        // create new Prop tag
        XmlTag newPropertyTag = rootTag.createChildTag('property', '', '', false)
        newPropertyTag.setAttribute('key', getUiLabelSafeValue(labelName))

        // Create base value for english lang
        XmlTag basePropertyValue = newPropertyTag.createChildTag('value', '', 'New Label', false)
        basePropertyValue.setAttribute('xml:lang', 'en')
        newPropertyTag.addSubTag(basePropertyValue, false)

        rootTag.addSubTag(newPropertyTag, false)
    }

    @Override
    void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(project)
        XmlAttribute attr = descriptor.psiElement as XmlAttribute
        String labelName = attr.value
        String componentName = getComponentName(attr)
        Map files = getLabelFilesList(ph, componentName)
        OfbizSimpleListDialog dial = new OfbizDialogBuilder(project)
                .from(files)
                .title(message('editor.action.create.label.title'))
                .text(message('editor.action.create.label.file.select'))
                .get()

        UiLabelFile fileToAddLabelIn

        if (application.unitTestMode) {
            fileToAddLabelIn = ph.collectAllUiLabelFilesInComponent(componentName).first()
        } else if (!(dial.showAndGet()) || (!dial.comboBoxValueOrKey)) {
            return
        } else {
            fileToAddLabelIn = dial.comboBoxValueOrKey as UiLabelFile
        }
        if (!fileToAddLabelIn) return
        addLabelInTargetFile(labelName, fileToAddLabelIn)
        fileToAddLabelIn.xmlElement.containingFile.navigate(true)
    }

}
