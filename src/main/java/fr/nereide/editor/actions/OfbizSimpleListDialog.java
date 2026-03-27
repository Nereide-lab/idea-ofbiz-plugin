/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * 'License') you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * 'AS IS' BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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

    public boolean isModal() {
        return true;
    }
}
