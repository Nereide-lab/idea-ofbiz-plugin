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
 * Ui dialog class used for selecting a value in drop down
 * Offers choices given in parameters
 */
public class GetUserChoiceFromList extends DialogWrapper {

    private JPanel myRoot;
    private JLabel myTextLabel;
    private ComboBox<String> myComboBox;

    public GetUserChoiceFromList(@Nullable Project project, String[] listChoices, String title, String text) {
        super(project, false);
        setTitle(title != null ? title : OfbizEditorBundle.message("editor.choice.title.default"));
        myTextLabel.setText(text != null ? text : OfbizEditorBundle.message("editor.choice.select.default"));
        myComboBox.setModel(new DefaultComboBoxModel<>(listChoices));
        myComboBox.setEditable(true);
        addUpdateListener(myComboBox);

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
        getOKAction().setEnabled(getComboBoxValue() != null && !getComboBoxValue().isEmpty());
    }

    public String getComboBoxValue() {
        final Object item = myComboBox.isEditable() ?
                myComboBox.getEditor().getItem() :
                myComboBox.getSelectedItem();
        if (item != null) {
            return ((String) item).trim();
        } else {
            return null;
        }
    }
}
