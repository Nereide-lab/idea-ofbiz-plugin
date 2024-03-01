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


    /**
     * retrieves a specified DomElement from a psi file
     * The dom implementation of the file MUST exist.
     * @param file the file to search the element in
     * @param dm A DomManager
     * @param wantedElementName the name of the element to look for
     * @param fileType the DomFileElement of the file to search in
     * @param elementNameGetter the name of the method to get a particuliar element
     * @param listGetterMethod the name of the method that gets all the elements
     * @return
     */
    PsiElement getElementFromSpecificFile(PsiFile file, DomManager dm, String wantedElementName, Class fileType,
                                                 String elementNameGetter, String listGetterMethod)

    /**
     * return the file targeted by string of type "component://..."
     * @param componentPathToFile
     * @param structureService
     * @return PsiFile if found or null
     */
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

    Grid getGrid(String name)

    Form getForm(String name)

    Property getProperty(String name)

    Screen getScreen(String name)

    List<Screen> getAllScreens()

    Menu getMenu(String name)

    PsiDirectory getComponentDir(String name)

    List<ComponentFile> getAllComponentsFiles()

    List<String> getAllComponentsNames()

    List<ExtendEntity> getExtendEntityListForEntity(String entityName)

    List<ExtendEntity> getAllExtendsEntity()

    Datasource getDatasource(String name)

    List<Screen> getAllScreenFromCurrentFileFromElement(XmlAttributeValue psiElement)

    List<Screen> getScreensFromScreenFile(XmlAttributeValue screenLocation)
}
