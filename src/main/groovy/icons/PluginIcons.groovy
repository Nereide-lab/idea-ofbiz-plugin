/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package icons

import static com.intellij.icons.AllIcons.Actions
import static com.intellij.icons.AllIcons.General
import static com.intellij.openapi.util.IconLoader.getIcon

import com.intellij.ui.LayeredIcon
import javax.swing.Icon

// codenarc-disable DuplicateNumberLiteral
/**
 * Icons used for the plugin
 */
class PluginIcons {

    static final Icon BASE_LOGO = getIcon('/icons/OfbizBaseLogoGrey.svg', PluginIcons)
    static final int TOTAL_WIDTH = 16
    static final int TOTAL_HEIGHT = 16
    static final int LABEL_WIDTH = 15
    static final int LABEL_HEIGHT = 7
    static final int MIDDLE = 8
    static final int LABEL_X_OFFSET = TOTAL_WIDTH - LABEL_WIDTH
    static final int LABEL_Y_OFFSET = TOTAL_HEIGHT - LABEL_HEIGHT

    static final Icon ENTITY_FILE_ICON = new LayeredIcon(2)
    static final Icon SERVICE_FILE_ICON = new LayeredIcon(2)
    static final Icon SCREEN_FILE_ICON = new LayeredIcon(2)
    static final Icon FORM_FILE_ICON = new LayeredIcon(2)
    static final Icon CONTROLLER_FILE_ICON = new LayeredIcon(2)
    static final Icon LABEL_FILE_ICON = new LayeredIcon(2)

    static final Icon ENTITY_ICON = new LayeredIcon(2)
    static final Icon VIEW_ENTITY_ICON = new LayeredIcon(2)
    static final Icon SERVICE_ICON = new LayeredIcon(2)

    static final Icon ECA_ICON = new LayeredIcon(2)
    static final Icon EXTENDED_ENTITY_ICON = new LayeredIcon(2)
    static {
        ENTITY_FILE_ICON.setIcon(BASE_LOGO, 0)
        ENTITY_FILE_ICON.setIcon(getIcon('/icons/ENT.svg', PluginIcons), 1, LABEL_X_OFFSET, LABEL_Y_OFFSET)

        SERVICE_FILE_ICON.setIcon(BASE_LOGO, 0)
        SERVICE_FILE_ICON.setIcon(getIcon('/icons/SVC.svg', PluginIcons), 1, LABEL_X_OFFSET, LABEL_Y_OFFSET)

        SCREEN_FILE_ICON.setIcon(BASE_LOGO, 0)
        SCREEN_FILE_ICON.setIcon(getIcon('/icons/SCN.svg', PluginIcons), 1, LABEL_X_OFFSET, LABEL_Y_OFFSET)

        FORM_FILE_ICON.setIcon(BASE_LOGO, 0)
        FORM_FILE_ICON.setIcon(getIcon('/icons/FRM.svg', PluginIcons), 1, LABEL_X_OFFSET, LABEL_Y_OFFSET)

        CONTROLLER_FILE_ICON.setIcon(BASE_LOGO, 0)
        CONTROLLER_FILE_ICON.setIcon(getIcon('/icons/CTL.svg', PluginIcons), 1, LABEL_X_OFFSET, LABEL_Y_OFFSET)

        LABEL_FILE_ICON.setIcon(BASE_LOGO, 0)
        LABEL_FILE_ICON.setIcon(getIcon('/icons/LBL.svg', PluginIcons), 1, LABEL_X_OFFSET, LABEL_Y_OFFSET)

        ENTITY_ICON.setIcon(BASE_LOGO, 0)
        ENTITY_ICON.setIcon(getIcon('/icons/BDD_Entity.svg', PluginIcons), 1, MIDDLE, MIDDLE)

        VIEW_ENTITY_ICON.setIcon(BASE_LOGO, 0)
        VIEW_ENTITY_ICON.setIcon(getIcon('/icons/BDD_View.svg', PluginIcons), 1, MIDDLE, MIDDLE)

        SERVICE_ICON.setIcon(BASE_LOGO, 0)
        SERVICE_ICON.setIcon(getIcon('/icons/Gear.svg', PluginIcons), 1, MIDDLE, MIDDLE)

        ECA_ICON.setIcon(BASE_LOGO, 0)
        ECA_ICON.setIcon(Actions.InlayGear, 1, MIDDLE, MIDDLE)

        EXTENDED_ENTITY_ICON.setIcon(ENTITY_ICON, 0)
        EXTENDED_ENTITY_ICON.setIcon(General.InlineAdd, 1, MIDDLE, MIDDLE)
    }

}
