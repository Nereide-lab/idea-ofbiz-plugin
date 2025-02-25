package fr.nereide.editor.actions;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.DocumentAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.util.Map;

/**
 * Ui dialog class used for selecting a value in drop down
 * Offers choices given in parameters
 * Handles lists and maps as inputs so that objects can be linked with keys.
 * Usable for example with file pointers, or dom elements for further handling
 */
public class OfbizSimpleListDialog extends DialogWrapper {

    private JPanel myRoot;
    private JLabel myTextLabel;
    private ComboBox<String> myComboBox;
    private final Map<String, Object> myChoices;

    public OfbizSimpleListDialog(@Nullable Project project, Map<String, Object> choicesList, String title, String text) {
        super(project, false);
        myChoices = choicesList;
        setTitle(title);
        myTextLabel.setText(text);
        myComboBox.setModel(new DefaultComboBoxModel<>(myChoices.keySet().toArray(new String[0])));
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
        getOKAction().setEnabled(getComboBoxKey() != null && !getComboBoxKey().isEmpty());
    }

    public String getComboBoxKey() {
        final Object key = myComboBox.isEditable() ?
                myComboBox.getEditor().getItem() :
                myComboBox.getSelectedItem();
        return key != null ? ((String) key).trim() : null;
    }

    public Object getComboBoxValueOrKey() {
        String key = getComboBoxKey();
        if (myChoices.containsKey(key) && myChoices.get(key) != null) {
            return myChoices.get(key);
        }
        return key;
    }
}
