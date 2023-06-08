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

package org.apache.ofbiz.idea.plugin.project

import com.intellij.psi.PsiDirectory
import org.apache.ofbiz.idea.plugin.dom.ComponentFile
import org.apache.ofbiz.idea.plugin.dom.ControllerFile
import org.apache.ofbiz.idea.plugin.dom.EntityEngineFile.Datasource
import org.apache.ofbiz.idea.plugin.dom.EntityModelFile.Entity
import org.apache.ofbiz.idea.plugin.dom.EntityModelFile.ExtendEntity
import org.apache.ofbiz.idea.plugin.dom.EntityModelFile.ViewEntity
import org.apache.ofbiz.idea.plugin.dom.FormFile
import org.apache.ofbiz.idea.plugin.dom.MenuFile
import org.apache.ofbiz.idea.plugin.dom.ScreenFile.Screen
import org.apache.ofbiz.idea.plugin.dom.ServiceDefFile
import org.apache.ofbiz.idea.plugin.dom.UiLabelFile

interface ProjectServiceInterface {

    ControllerFile.RequestMap getRequestMap(String name)

    ControllerFile.ViewMap getViewMap(String name)

    Entity getEntity(String name)

    List<Entity> getAllEntities()

    ViewEntity getViewEntity(String name)

    List<ViewEntity> getAllViewEntities()

    ServiceDefFile.Service getService(String name)

    List<ServiceDefFile.Service> getAllServices()

    FormFile.Grid getGrid(String name)

    FormFile.Form getForm(String name)

    UiLabelFile.Property getProperty(String name)

    Screen getScreen(String name)

    MenuFile.Menu getMenu(String name)

    PsiDirectory getComponentDir(String name)

    List<ComponentFile> getAllComponentsFiles()

    List<String> getAllComponentsNames()

    List<ExtendEntity> getExtendEntityListForEntity(String entityName)

    List<ExtendEntity> getAllExtendsEntity()

    Datasource getDatasource(String name)
}
