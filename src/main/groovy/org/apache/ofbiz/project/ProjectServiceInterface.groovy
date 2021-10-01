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

package org.apache.ofbiz.project

import com.intellij.psi.PsiDirectory
import org.apache.ofbiz.dom.ControllerFile.ViewMap
import org.apache.ofbiz.dom.ControllerFile.RequestMap
import org.apache.ofbiz.dom.EntityModelFile.Entity
import org.apache.ofbiz.dom.EntityModelFile.ViewEntity
import org.apache.ofbiz.dom.FormFile.Grid
import org.apache.ofbiz.dom.FormFile.Form
import org.apache.ofbiz.dom.MenuFile.Menu
import org.apache.ofbiz.dom.ScreenFile.Screen
import org.apache.ofbiz.dom.ServiceDefFile.Service
import org.apache.ofbiz.dom.UiLabelFile.Property

interface ProjectServiceInterface {

    RequestMap getControllerUri(String name)

    ViewMap getControllerViewName(String name)

    Entity getEntity(String name)

    ViewEntity getViewEntity(String name)

    Service getService(String name)

    Grid getGrid(String name)

    Form getForm(String name)

    Property getProperty(String name)

    Screen getScreen(String name)

    Menu getMenu(String name)

    PsiDirectory getComponentDir(String name)

}
