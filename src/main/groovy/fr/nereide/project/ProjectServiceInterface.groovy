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

package fr.nereide.project

import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import com.intellij.util.xml.DomManager
import fr.nereide.dom.ComponentFile
import fr.nereide.dom.ControllerFile.RequestMap
import fr.nereide.dom.ControllerFile.ViewMap
import fr.nereide.dom.EntityEngineFile.Datasource
import fr.nereide.dom.EntityModelFile.Entity
import fr.nereide.dom.EntityModelFile.ExtendEntity
import fr.nereide.dom.EntityModelFile.ViewEntity
import fr.nereide.dom.FormFile.Form
import fr.nereide.dom.FormFile.Grid
import fr.nereide.dom.MenuFile.Menu
import fr.nereide.dom.ScreenFile.Screen
import fr.nereide.dom.ServiceDefFile.Service
import fr.nereide.dom.UiLabelFile.Property

interface ProjectServiceInterface {


    PsiFile getPsiFileAtLocation(String componentPathToFile)

    RequestMap getRequestMap(String name)

    ViewMap getViewMap(String name)

    Entity getEntity(String name)

    List<Entity> getAllEntities()

    ViewEntity getViewEntity(String name)

    List<ViewEntity> getAllViewEntities()

    Service getService(String name)

    List<Service> getServices(String name)

    List<Service> getAllServices()

    Property getProperty(String name)

    PsiDirectory getComponentDir(String name)

    List<ComponentFile> getAllComponentsFiles()

    List<String> getAllComponentsNames()

    List<ExtendEntity> getExtendEntityListForEntity(String entityName)

    Datasource getDatasource(String name)

    List<Screen> getAllScreenFromCurrentFileFromElement(XmlAttributeValue psiElement)

    List<Screen> getScreensFromScreenFileAtLocation(XmlElement screenLocation, boolean isController)

    Screen getScreenFromFileAtLocation(DomManager dm, String componentPathToFile, String screenName)

    Screen getScreenFromPsiFile(DomManager dm, PsiFile file, String screenName)

    Form getFormFromFileAtLocation(DomManager dm, String componentPathToFile, String formName)

    Form getFormFromPsiFile(DomManager dm, PsiFile file, String formName)

    Grid getGridFromFileAtLocation(DomManager dm, String componentPathToFile, String formName)

    Grid getGridFromPsiFile(DomManager dm, PsiFile file, String formName)

    Menu getMenuFromFileAtLocation(DomManager dm, String componentPathToFile, String menuName)

    Menu getMenuFromPsiFile(DomManager dm, PsiFile file, String menuName)

}
