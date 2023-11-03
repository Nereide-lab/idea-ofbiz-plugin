package fr.nereide.editor.actions;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.DocumentAdapter;
import fr.nereide.editor.OfbizEditorBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;

/**
 * Ui dialog class used for selecting the file type to create.
 * Offers choices for each file type of OFBiz
 */
public class CreateOfbizFileAction extends DialogWrapper {

    private JPanel myRoot;

    private JLabel myFileTypeLabel;
    private ComboBox<String> myFileTypeComboBox;

    public CreateOfbizFileAction(@Nullable Project project, String[] listChoices) {
        super(project, false);
        setTitle(OfbizEditorBundle.message("editor.action.create.ofbiz.file.title"));
        myFileTypeLabel.setText(OfbizEditorBundle.message("editor.action.create.ofbiz.file.select"));
        myFileTypeComboBox.setModel(new DefaultComboBoxModel<>(listChoices));
        myFileTypeComboBox.setEditable(true);
        myFileTypeComboBox.setSelectedItem("");
        addUpdateListener(myFileTypeComboBox);

        updateOkAction();
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return myRoot;
    }


    private void addUpdateListener(ComboBox<String> comboBox) {
        final ComboBoxEditor boxEditor = comboBox.getEditor();
        if (boxEditor != null) {
            final Component component = boxEditor.getEditorComponent();
            if (component instanceof JTextField) {
                ((JTextField) component).getDocument().addDocumentListener(new DocumentAdapter() {
                    @Override
                    protected void textChanged(@NotNull DocumentEvent e) {
                        updateOkAction();
                    }
                });
            }
        }
        comboBox.addItemListener(e -> updateOkAction());
    }

    private void updateOkAction() {
        getOKAction().setEnabled(getFileType() != null && !getFileType().isEmpty());
    }

    public String getFileType() {
        final Object item = (myFileTypeComboBox.isEditable() ?
                myFileTypeComboBox.getEditor().getItem() :
                myFileTypeComboBox.getSelectedItem());
        if (item != null) {
            return ((String) item).trim();
        } else {
            return null;
        }
    }
}
