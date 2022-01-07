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

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader
import com.intellij.ui.LayeredIcon

import javax.swing.Icon


class PluginIcons {
    static final Icon BASE_LOGO = IconLoader.getIcon('/icons/OfbizBaseLogoGrey.svg', PluginIcons.class)
    static final int TOTAL_WIDTH = 16
    static final int TOTAL_HEIGHT = 16
    static final int LABEL_WIDTH = 15
    static final int LABEL_HEIGHT = 7
    static final int LABEL_X_OFFSET = TOTAL_WIDTH - LABEL_WIDTH
    static final int LABEL_Y_OFFSET = TOTAL_HEIGHT - LABEL_HEIGHT

    static final Icon ENTITY_FILE_ICON = new LayeredIcon(2)
    static final Icon SERVICE_FILE_ICON = new LayeredIcon(2)
    static final Icon SCREEN_FILE_ICON = new LayeredIcon(2)
    static final Icon FORM_FILE_ICON = new LayeredIcon(2)
    static final Icon CONTROLLER_FILE_ICON = new LayeredIcon(2)
    static final Icon LABEL_FILE_ICON = new LayeredIcon(2)

    static {
        ENTITY_FILE_ICON.setIcon(BASE_LOGO, 0)
        ENTITY_FILE_ICON.setIcon(IconLoader.getIcon('/icons/ENT.svg', PluginIcons.class),
                1, LABEL_X_OFFSET, LABEL_Y_OFFSET)

        SERVICE_FILE_ICON.setIcon(BASE_LOGO, 0)
        SERVICE_FILE_ICON.setIcon(IconLoader.getIcon('/icons/SVC.svg', PluginIcons.class),
                1, LABEL_X_OFFSET, LABEL_Y_OFFSET)

        SCREEN_FILE_ICON.setIcon(BASE_LOGO, 0)
        SCREEN_FILE_ICON.setIcon(IconLoader.getIcon('/icons/SCN.svg', PluginIcons.class),
                1, LABEL_X_OFFSET, LABEL_Y_OFFSET)

        FORM_FILE_ICON.setIcon(BASE_LOGO, 0)
        FORM_FILE_ICON.setIcon(IconLoader.getIcon('/icons/FRM.svg', PluginIcons.class),
                1, LABEL_X_OFFSET, LABEL_Y_OFFSET)

        CONTROLLER_FILE_ICON.setIcon(BASE_LOGO, 0)
        CONTROLLER_FILE_ICON.setIcon(IconLoader.getIcon('/icons/CTL.svg', PluginIcons.class),
                1, LABEL_X_OFFSET, LABEL_Y_OFFSET)

        LABEL_FILE_ICON.setIcon(BASE_LOGO, 0)
        LABEL_FILE_ICON.setIcon(IconLoader.getIcon('/icons/LBL.svg', PluginIcons.class),
                1, LABEL_X_OFFSET, LABEL_Y_OFFSET)

    }
}
