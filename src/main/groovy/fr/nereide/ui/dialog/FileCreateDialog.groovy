package fr.nereide.ui.dialog

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.refactoring.ui.MemberSelectionPanel
import org.jetbrains.annotations.Nullable

import javax.swing.JComponent

class FileCreateDialog extends DialogWrapper {

    FileCreateDialog(@Nullable Project project) {
        super(project, true)
    }

    @Override
    protected JComponent createCenterPanel() {
        return null
    }
}
