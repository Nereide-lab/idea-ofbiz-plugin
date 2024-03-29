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

class UiLabelFileDescription<S extends DomElement> extends DomFileDescription {
    private static final String rootTagName = "resource"
    public static final String XML_LANG_NS = 'http://www.w3.org/XML/1998/namespace'
    public static final String XML_LANG_NS_NAME = 'xml'

    UiLabelFileDescription() {
        super(UiLabelFile.class, rootTagName)
    }

    @Nullable
    Icon getFileIcon(int flags) {
        return PluginIcons.LABEL_FILE_ICON
    }

    @Override
    protected void initializeFileDescription() {
        registerNamespacePolicy(XML_LANG_NS_NAME, XML_LANG_NS)
    }
}
