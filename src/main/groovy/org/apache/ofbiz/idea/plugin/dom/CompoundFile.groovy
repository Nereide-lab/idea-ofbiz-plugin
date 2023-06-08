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
import com.intellij.util.xml.Namespace
import com.intellij.util.xml.SubTag

interface CompoundFile extends DomElement {

    @Namespace(CompoundFileDescription.FORM_NS)
    interface FormBlocInCpd extends FormFile {}

    @Namespace(CompoundFileDescription.SITE_CONF_NS)
    interface ControlerBlocInCpd extends ControllerFile {}

    @Namespace(CompoundFileDescription.MENU_NS)
    interface MenuBlocInCpd extends MenuFile {}

    @Namespace(CompoundFileDescription.SCREEN_NS)
    interface ScreenBlocInCpd extends ScreenFile {}

    @SubTag('forms')
    FormBlocInCpd getForms()

    @SubTag('site-conf')
    ControlerBlocInCpd getSiteConf()

    @SubTag('menus')
    MenuBlocInCpd getMenus()

    @SubTag('screens')
    ScreenBlocInCpd getScreens()
}
