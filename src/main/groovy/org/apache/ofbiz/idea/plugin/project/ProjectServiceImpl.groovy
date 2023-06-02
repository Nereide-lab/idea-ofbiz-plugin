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

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileElement
import com.intellij.util.xml.DomService
import org.apache.ofbiz.idea.plugin.dom.ComponentFile
import org.apache.ofbiz.idea.plugin.dom.EntityEngineFile
import org.apache.ofbiz.idea.plugin.dom.EntityEngineFile.Datasource
import org.apache.ofbiz.idea.plugin.dom.EntityModelFile
import org.apache.ofbiz.idea.plugin.dom.EntityModelFile.Entity
import org.apache.ofbiz.idea.plugin.dom.EntityModelFile.ViewEntity
import org.apache.ofbiz.idea.plugin.dom.EntityModelFile.ExtendEntity
import org.apache.ofbiz.idea.plugin.dom.ScreenFile
import org.apache.ofbiz.idea.plugin.dom.ScreenFile.Screen

import static java.util.stream.Collectors.*

class ProjectServiceImpl implements ProjectServiceInterface {
    private final Project project

    ProjectServiceImpl(Project project) {
        this.project = project
    }

    org.apache.ofbiz.idea.plugin.dom.ControllerFile.RequestMap getRequestMap(String name) {
        return getClassMatchingProjectDomElement(org.apache.ofbiz.idea.plugin.dom.ControllerFile.class, project, name, "getRequestMaps", "getUri")
    }

    org.apache.ofbiz.idea.plugin.dom.ControllerFile.ViewMap getViewMap(String name) {
        return getClassMatchingProjectDomElement(org.apache.ofbiz.idea.plugin.dom.ControllerFile.class, project, name, "getViewMaps", "getName")
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

    org.apache.ofbiz.idea.plugin.dom.ServiceDefFile.Service getService(String name) {
        return getMatchingElementFromXmlFiles(org.apache.ofbiz.idea.plugin.dom.ServiceDefFile.class, "getServices", "getName", name)
    }

    List<org.apache.ofbiz.idea.plugin.dom.ServiceDefFile.Service> getAllServices() {
        return getAllElementOfSpecificType(org.apache.ofbiz.idea.plugin.dom.ServiceDefFile.class, "getServices")
    }

    org.apache.ofbiz.idea.plugin.dom.UiLabelFile.Property getProperty(String name) {
        return getMatchingElementFromXmlFiles(org.apache.ofbiz.idea.plugin.dom.UiLabelFile.class, "getProperties", "getKey", name)
    }

    org.apache.ofbiz.idea.plugin.dom.FormFile.Grid getGrid(String name) {
        return getMatchingElementFromXmlFiles(org.apache.ofbiz.idea.plugin.dom.FormFile.class, "getGrids", "getName", name)
    }

    org.apache.ofbiz.idea.plugin.dom.FormFile.Form getForm(String name) {
        return getMatchingElementFromXmlFiles(org.apache.ofbiz.idea.plugin.dom.FormFile.class, "getForms", "getName", name)
    }

    Screen getScreen(String name) {
        return getMatchingElementFromXmlFiles(ScreenFile.class, "getScreens", "getName", name)
    }

    org.apache.ofbiz.idea.plugin.dom.MenuFile.Menu getMenu(String name) {
        return getMatchingElementFromXmlFiles(org.apache.ofbiz.idea.plugin.dom.MenuFile.class, "getMenus", "getName", name)
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
        List<org.apache.ofbiz.idea.plugin.dom.CompoundFile> cpdFiles = DomService.getInstance().getFileElements(org.apache.ofbiz.idea.plugin.dom.CompoundFile.class, project, GlobalSearchScope.allScope(project)) as List<org.apache.ofbiz.idea.plugin.dom.CompoundFile>
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
