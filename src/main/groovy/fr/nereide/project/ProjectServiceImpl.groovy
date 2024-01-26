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

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileElement
import com.intellij.util.xml.DomService
import fr.nereide.dom.ComponentFile
import fr.nereide.dom.CompoundFile
import fr.nereide.dom.ControllerFile
import fr.nereide.dom.ControllerFile.ViewMap
import fr.nereide.dom.ControllerFile.RequestMap
import fr.nereide.dom.EntityEngineFile
import fr.nereide.dom.EntityEngineFile.Datasource
import fr.nereide.dom.EntityModelFile
import fr.nereide.dom.EntityModelFile.Entity
import fr.nereide.dom.EntityModelFile.ViewEntity
import fr.nereide.dom.EntityModelFile.ExtendEntity
import fr.nereide.dom.FormFile
import fr.nereide.dom.FormFile.Form
import fr.nereide.dom.FormFile.Grid
import fr.nereide.dom.MenuFile
import fr.nereide.dom.MenuFile.Menu
import fr.nereide.dom.ScreenFile
import fr.nereide.dom.ScreenFile.Screen
import fr.nereide.dom.ServiceDefFile
import fr.nereide.dom.ServiceDefFile.Service
import fr.nereide.dom.UiLabelFile
import fr.nereide.dom.UiLabelFile.Property

import static java.util.stream.Collectors.*

class ProjectServiceImpl implements ProjectServiceInterface {
    private final Project project

    ProjectServiceImpl(Project project) {
        this.project = project
    }

    RequestMap getRequestMap(String name) {
        return getClassMatchingProjectDomElement(ControllerFile.class, project, name, "getRequestMaps", "getUri")
    }

    ViewMap getViewMap(String name) {
        return getClassMatchingProjectDomElement(ControllerFile.class, project, name, "getViewMaps", "getName")
    }

    Entity getEntity(String name) {
        return getMatchingElementFromXmlFiles(EntityModelFile.class, "getEntities", "getEntityName", name)
    }

    List<Entity> getAllEntities() {
        return getAllElementOfSpecificType(EntityModelFile.class, "getEntities")
    }

    ViewEntity getViewEntity(String name) {
        return getMatchingElementFromXmlFiles(EntityModelFile.class, "getViewEntities", "getEntityName", name)
    }

    List<ViewEntity> getAllViewEntities() {
        return getAllElementOfSpecificType(EntityModelFile.class, "getViewEntities")
    }

    Service getService(String name) {
        return getMatchingElementFromXmlFiles(ServiceDefFile.class, "getServices", "getName", name)
    }

    List<Service> getServices(String name) {
        List toReturn = []
        List<DomFileElement<ServiceDefFile>> serviceDefFiles = DomService.getInstance().getFileElements(ServiceDefFile.class, project, GlobalSearchScope.allScope(project))
        for (DomFileElement<ServiceDefFile> serviceDefFile : serviceDefFiles) {
            List<DomElement> servicesDomElements = serviceDefFile.getRootElement().getServices()
            List matching = servicesDomElements.findAll { service ->
                service.getName().getValue().equalsIgnoreCase(name)
            }
            if (matching) toReturn.addAll(matching)
        }
        return toReturn
    }

    List<Service> getAllServices() {
        return getAllElementOfSpecificType(ServiceDefFile.class, "getServices")
    }

    Property getProperty(String name) {
        return getMatchingElementFromXmlFiles(UiLabelFile.class, "getProperties", "getKey", name)
    }

    Grid getGrid(String name) {
        return getMatchingElementFromXmlFiles(FormFile.class, "getGrids", "getName", name)
    }

    Form getForm(String name) {
        return getMatchingElementFromXmlFiles(FormFile.class, "getForms", "getName", name)
    }

    Screen getScreen(String name) {
        return getMatchingElementFromXmlFiles(ScreenFile.class, "getScreens", "getName", name)
    }

    List<Screen> getAllScreens() {
        return getAllElementOfSpecificType(ScreenFile.class, "getScreens")
    }

    Menu getMenu(String name) {
        return getMatchingElementFromXmlFiles(MenuFile.class, "getMenus", "getName", name)
    }

    Datasource getDatasource(String name) {
        return getMatchingElementFromXmlFiles(EntityEngineFile.class, "getDatasources", "getName", name)
    }

    static boolean isInTestDir(DomFileElement file) {
        String dir = file.getFile().getContainingDirectory().toString()
        return dir.contains('/tests')
    }

    PsiDirectory getComponentDir(String name) {
        List<ComponentFile> componentFiles = getAllComponentsFiles()
        DomFileElement relevantComponent = componentFiles.stream().find { componentFile ->
            componentFile.getRootElement().getName().getValue().equalsIgnoreCase(name)
        }
        if (relevantComponent) {
            return relevantComponent.getFile().getContainingDirectory()
        }
        return null
    }

    @Override
    List<ExtendEntity> getExtendEntityListForEntity(String entityName) {
        List<DomElement> extendList = getAllElementOfSpecificType(EntityModelFile.class, "getExtendEntities")
        return extendList.stream().filter {
            it.getEntityName().getValue() == entityName
        }.collect() as List<ExtendEntity>
    }

    @Override
    List<ExtendEntity> getAllExtendsEntity() {
        return getAllElementOfSpecificType(EntityModelFile.class, "getExtendEntities")
    }

    @Override
    List<ComponentFile> getAllComponentsFiles() {
        return DomService.getInstance()
                .getFileElements(ComponentFile.class, project, GlobalSearchScope.allScope(project))
                .findAll { !isInTestDir(it) }
                .toList() as List<ComponentFile>
    }

    @Override
    List<String> getAllComponentsNames() {
        return DomService.getInstance()
                .getFileElements(ComponentFile.class, project, GlobalSearchScope.allScope(project))
                .findAll { !isInTestDir(it) }
                .collect { it.getRootElement() }
                .collect { it.name.value }
    }

    private DomElement getMatchingElementFromXmlFiles(Class classFile,
                                                      String elementListGetterMethod,
                                                      String elementValueGetterName,
                                                      String matchingValue) {
        List<DomFileElement<?>> projectFiles = getClassMatchingProjectDomBlocs(classFile, this.project)
        for (DomFileElement<?> projectFile : projectFiles) {
            List<DomElement> elements = projectFile.getRootElement()."$elementListGetterMethod"()
            for (DomElement element : elements) {
                if (element."$elementValueGetterName"().getValue().equalsIgnoreCase(matchingValue)) return element
            }
        }
        return null
    }

    private List<DomElement> getAllElementOfSpecificType(Class classFile, String elementListGetterMethod) {
        List resultSet = []
        List<DomFileElement<?>> projectFiles = getClassMatchingProjectDomBlocs(classFile, this.project)
        for (DomFileElement<?> projectFile : projectFiles) {
            List<DomElement> elements = projectFile.getRootElement()."$elementListGetterMethod"()
            for (DomElement element : elements) {
                resultSet << element
            }
        }
        return resultSet
    }

    private static List<DomFileElement> getClassMatchingProjectDomBlocs(Class classFile, Project project) {
        return DomService.getInstance().getFileElements(classFile, project, GlobalSearchScope.allScope(project))
    }

    private static List<DomElement> getClassMatchingProjectDomElementList(Class classFile, Project project, String elementListGetterMethod) {
        List<DomFileElement> relevantFile = getClassMatchingProjectDomBlocs(classFile, project)
        List<DomElement> relevantDomBlocs = []
        relevantFile.forEach {
            relevantDomBlocs << it.getRootElement()
        }
        List<CompoundFile> cpdFiles = DomService.getInstance().getFileElements(CompoundFile.class, project, GlobalSearchScope.allScope(project)) as List<CompoundFile>
        cpdFiles.forEach {
            relevantDomBlocs << it.getRootElement().getSiteConf()
        }
        List toReturn = []
        relevantDomBlocs.forEach {
            if (it."$elementListGetterMethod"()) {
                toReturn.addAll(it."$elementListGetterMethod"())
            }
        }
        return toReturn
    }

    private static DomElement getClassMatchingProjectDomElement(Class classFile, Project project, String wantedName,
                                                                String elementListGetterMethod, String elementNameGetterMethod) {
        List<DomElement> relevantDomBlocs = getClassMatchingProjectDomElementList(classFile, project, elementListGetterMethod)
        List<DomElement> domEls = relevantDomBlocs.stream().filter { DomElement it ->
            wantedName.equalsIgnoreCase(it."$elementNameGetterMethod"().getValue())
        }.collect(toList())
        // TODO : Right now we only return the first element found. We'll have to add control for the right component
        return domEls.size() > 0 ? domEls[0] : null
    }
}
