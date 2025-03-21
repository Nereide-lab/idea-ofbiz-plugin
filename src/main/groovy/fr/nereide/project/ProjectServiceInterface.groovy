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
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import com.intellij.util.xml.DomElement
import fr.nereide.dom.element.controller.RequestMap
import fr.nereide.dom.element.controller.ViewMap
import fr.nereide.dom.element.entityengine.Datasource
import fr.nereide.dom.element.entitymodel.Entity
import fr.nereide.dom.element.entitymodel.EntityRelation
import fr.nereide.dom.element.entitymodel.ExtendEntity
import fr.nereide.dom.element.entitymodel.ViewEntity
import fr.nereide.dom.element.form.Form
import fr.nereide.dom.element.form.Grid
import fr.nereide.dom.element.menu.Menu
import fr.nereide.dom.element.screen.Screen
import fr.nereide.dom.element.service.Service
import fr.nereide.dom.element.uilabel.Property
import fr.nereide.dom.file.ComponentFile
import fr.nereide.dom.file.UiLabelFile

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

    List<Screen> getAllScreenFromCurrentFileFromElement(XmlElement psiElement)

    List<Screen> getScreensFromScreenFileAtLocation(XmlElement screenLocation, boolean isController)

    Screen getScreenFromFileAtLocation(String componentPathToFile, String screenName)

    Screen getScreenFromPsiFile(PsiFile file, String screenName)

    Form getFormFromFileAtLocation(String componentPathToFile, String formName)

    Form getFormFromPsiFile(PsiFile file, String formName)

    List<DomElement> getDomElementListFromFileAtLocation(String componentPathToFile, Class fileType)

    List<DomElement> getDomElementListFromFileAtLocation(String componentPathToFile, Class fileType, String wantedElement)

    List<Form> getAllFormsFromCurrentFileFromElement(XmlElement myVal)

    List<Menu> getAllMenuFromCurrentFileFromElement(XmlElement myVal)

    Grid getGridFromFileAtLocation(String componentPathToFile, String formName)

    Grid getGridFromPsiFile(PsiFile file, String formName)

    Menu getMenuFromFileAtLocation(String componentPathToFile, String menuName)

    Menu getMenuFromPsiFile(PsiFile file, String menuName)

    List<RequestMap> getComponentRequestMaps(String componentName)

    Map<String, List<String>> getAllMountPointsAndRequestMaps(PsiElement myElement)

    List<EntityRelation> getAllEntityRelations()

    List<UiLabelFile> getAllUiLabelFiles()

    List<UiLabelFile> getAllUiLabelFilesInComponent(String componentName)
}
