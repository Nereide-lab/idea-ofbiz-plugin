/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.ofbiz.idea.plugin.dom

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileDescription

class CompoundFileDescription<S extends DomElement> extends DomFileDescription<CompoundFile> {
    private static final String rootTagName = "compound-widgets"

    public static final String SITE_CONF_NS_URL = 'http://ofbiz.apache.org/Site-Conf'
    public static final String SITE_CONF_NS = 'sc'
    public static final String SITE_CONF_NS_PREFIX = 'sc:'

    public static final String FORM_NS_URL = 'http://ofbiz.apache.org/Widget-Form'
    public static final String FORM_NS = 'wf'
    public static final String FORM_NS_PREFIX = 'wf:'

    public static final String MENU_NS_URL = 'http://ofbiz.apache.org/Widget-Menu'
    public static final String MENU_NS = 'wm'

    public static final String SCREEN_NS_URL = 'http://ofbiz.apache.org/Widget-Screen'
    public static final String SCREEN_NS = 'ws'
    public static final String SCREEN_NS_PREFIX = 'ws:'

    CompoundFileDescription() { super(CompoundFile.class, rootTagName) }

    @Override
    protected void initializeFileDescription() {
        registerNamespacePolicy(SITE_CONF_NS, SITE_CONF_NS_URL)
        registerNamespacePolicy(FORM_NS, FORM_NS_URL)
        registerNamespacePolicy(MENU_NS, MENU_NS_URL)
        registerNamespacePolicy(SCREEN_NS, SCREEN_NS_URL)
    }
}
