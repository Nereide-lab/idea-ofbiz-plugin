/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License") you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package fr.nereide.dom

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileDescription
import icons.PluginIcons
import org.jetbrains.annotations.Nullable

import javax.swing.Icon

class ControllerFileDescription<S extends DomElement> extends DomFileDescription {
    private static final String rootTagName = "site-conf"

    ControllerFileDescription() {
        super(ControllerFile.class, rootTagName)
    }

    @Nullable
    Icon getFileIcon(int flags) {
        return PluginIcons.CONTROLLER_FILE_ICON
    }
}
