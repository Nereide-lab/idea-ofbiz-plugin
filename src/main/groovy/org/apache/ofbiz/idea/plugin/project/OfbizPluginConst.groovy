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

package org.apache.ofbiz.idea.plugin.project

import com.intellij.patterns.PsiJavaElementPattern
import com.intellij.patterns.PsiJavaPatterns
import com.intellij.psi.PsiLiteral

class OfbizPluginConst {
    // classes
    public static final String DISPATCH_CONTEXT_CLASS = 'org.apache.ofbiz.service.DispatchContext'
    public static final String LOCAL_DISPATCHER_CLASS = 'org.apache.ofbiz.service.LocalDispatcher'
    public static final String SCRIPT_HELPER_CLASS = 'org.apache.ofbiz.base.util.ScriptHelper'
    public static final String DELEGATOR_CLASS = 'org.apache.ofbiz.entity.Delegator'
    public static final String DYNAMIC_VIEW_ENTITY_CLASS = 'org.apache.ofbiz.entity.model.DynamicViewEntity'
    public static final String ENTITY_DATA_SERVICES_CLASS = 'org.apache.ofbiz.entityext.data.EntityDataServices'
    public static final String ENTITY_QUERY_CLASS = 'org.apache.ofbiz.entity.util.EntityQuery'
    public static final String UTIL_PROPERTIES_CLASS = 'org.apache.ofbiz.base.util.UtilProperties'
    public static final String GENERIC_VALUE_CLASS = 'org.apache.ofbiz.entity.GenericValue'
    public static final String GENERIC_ENTITY_CLASS = 'org.apache.ofbiz.entity.GenericEntity'
    public static final String MODEL_KEYMAP_CLASS = 'org.apache.ofbiz.entity.model.ModelKeyMap'

    // Strings
    public static final String QUERY_FROM_STATEMENT = 'from('
    public static final String DYNAMIC_VIEW_ENTITY_CLASS_NAME = 'DynamicViewEntity'
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
    public static final String XML_LANG_NS = 'http://www.w3.org/XML/1998/namespace'
    public static final String XML_LANG_NS_NAME = 'xml'

    static Object makeMethodParameterPattern(PsiJavaElementPattern<? extends PsiLiteral, ?> elementPattern,
                                             String methodName, String className, int index) {
        return elementPattern.methodCallParameter(index, PsiJavaPatterns.psiMethod().withName(methodName).definedInClass(className))
    }
}
