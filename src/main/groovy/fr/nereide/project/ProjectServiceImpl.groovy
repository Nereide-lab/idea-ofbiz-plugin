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

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlFile
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileElement
import com.intellij.util.xml.DomManager
import com.intellij.util.xml.DomService
import fr.nereide.dom.element.Webapp
import fr.nereide.dom.element.controller.Include
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
import fr.nereide.dom.file.*
import fr.nereide.project.utils.FileHandlingUtils
import fr.nereide.project.utils.MiscUtils
import fr.nereide.reference.common.ComponentAwareFileReferenceSet

import java.util.regex.Matcher

import static com.intellij.psi.search.GlobalSearchScope.allScope
import static com.intellij.psi.search.GlobalSearchScopesCore.directoryScope

class ProjectServiceImpl implements ProjectServiceInterface {
    private static final Logger LOG = Logger.getInstance(ProjectServiceImpl.class)

    private final Project project
    private final DomManager domManager
    private final DomService domService

    ProjectServiceImpl(Project project) {
        this.project = project
        this.domManager = DomManager.getDomManager(project)
        this.domService = DomService.getInstance()
    }

    RequestMap getRequestMap(String name) {
        List<ControllerFile> relevantDomBlocs = getAllControllerFiles()
        return relevantDomBlocs.collect { ControllerFile cf -> cf.requestMaps }
                .flatten()
                .find { RequestMap req -> req.uri?.value == name } as RequestMap
    }

    ViewMap getViewMap(String name) {
        List<ControllerFile> relevantDomBlocs = getAllControllerFiles()
        return relevantDomBlocs.collect { ControllerFile cf -> cf.viewMaps }
                .flatten()
                .find { ViewMap vm -> vm.name?.value == name } as ViewMap
    }

    private List<ControllerFile> getAllControllerFiles() {
        return (
                domService.getFileElements(ControllerFile.class, project, allScope(project))
                        .collect { DomFileElement<ControllerFile> cf -> cf.rootElement }
                        +
                        domService.getFileElements(CompoundFile.class, project, allScope(project))
                                .collect { DomFileElement<CompoundFile> cpd -> cpd.rootElement.siteConf }
        )
    }

    Entity getEntity(String name) {
        return getAllEntities().find { Entity e -> e.entityName.value == name }
    }

    List<Entity> getAllEntities() {
        return domService.getFileElements(EntityModelFile.class, project, allScope(project))
                .collect { DomFileElement<EntityModelFile> emf -> emf.rootElement.entities }
                .flatten() as List<Entity>
    }

    ViewEntity getViewEntity(String name) {
        return getAllViewEntities().find { ViewEntity e -> e.entityName.value == name }
    }

    List<ViewEntity> getAllViewEntities() {
        return domService.getFileElements(EntityModelFile.class, project, allScope(project))
                .collect { DomFileElement<EntityModelFile> emf -> emf.rootElement.viewEntities }
                .flatten() as List<ViewEntity>
    }

    Service getService(String name) {
        return getAllServices().find { Service s -> s.name.value == name } as Service
    }

    List<Service> getServices(String name) {
        return getAllServices().findAll { Service s -> s.name.value == name } as List<Service>
    }

    List<Service> getAllServices() {
        return domService.getFileElements(ServiceFile.class, project, allScope(project))
                .collect { DomFileElement<ServiceFile> sdf -> sdf.rootElement.services }
                .flatten() as List<Service>
    }

    Property getProperty(String name) {
        return domService.getFileElements(UiLabelFile.class, project, allScope(project))
                .collect { DomFileElement<UiLabelFile> ulf -> ulf.rootElement.properties }
                .flatten()
                .find { Property p -> p.key.value == name } as Property
    }

    Datasource getDatasource(String name) {
        return domService.getFileElements(EntityEngineFile.class, project, allScope(project))
                .collect { DomFileElement<EntityEngineFile> eef -> eef.rootElement.datasources }
                .flatten()
                .find { Datasource d -> d.name.value == name } as Datasource
    }

    List<Form> getAllFormsFromCurrentFileFromElement(XmlAttributeValue myVal) {
        DomFileElement<FormFile> screenFile = domManager.getFileElement(myVal.getContainingFile() as XmlFile, FormFile.class)
        return screenFile.rootElement.forms
    }

    List<Screen> getAllScreenFromCurrentFileFromElement(XmlElement myVal) {
        DomFileElement<ScreenFile> screenFile = domManager.getFileElement(myVal.getContainingFile() as XmlFile, ScreenFile.class)
        return screenFile.rootElement.screens
    }

    List<Screen> getScreensFromScreenFileAtLocation(XmlElement screenLocation, boolean isController) {
        String location
        if (isController) {
            String locationText = screenLocation.text
            location = locationText.substring(0, locationText.lastIndexOf('#'))
        } else {
            location = (screenLocation as XmlAttributeValue).getValue()
        }
        PsiFile file = getPsiFileAtLocation(location)
        DomFileElement<ScreenFile> screenFile = domManager.getFileElement(file as XmlFile, ScreenFile.class)
        if (screenFile) {
            return screenFile.getRootElement().getScreens()
        }
        return domManager.getFileElement(file as XmlFile, CompoundFile.class).getRootElement().getScreens().getScreens()
    }

    static boolean isInTestDir(DomFileElement file) {
        String dir = file.getFile().getContainingDirectory().toString()
        return dir.contains('/tests')
    }

    PsiDirectory getComponentDir(String name) {
        List<DomFileElement<ComponentFile>> componentFiles = getAllComponentsFiles()
        DomFileElement<ComponentFile> relevantComponent = componentFiles
                .find { DomFileElement<ComponentFile> componentFile ->
                    componentFile.rootElement.name.value.equalsIgnoreCase(name)
                }
        if (relevantComponent) {
            return relevantComponent.getFile().getContainingDirectory()
        }
        return null
    }

    @Override
    List<ExtendEntity> getExtendEntityListForEntity(String entityName) {
        return domService.getFileElements(EntityModelFile.class, project, allScope(project))
                .collect { DomFileElement<EntityModelFile> emf -> emf.rootElement.extendEntities }
                .flatten()
                .findAll { ExtendEntity ee -> ee.entityName.value == entityName }
                .collect() as List<ExtendEntity>

    }

    @Override
    List<DomFileElement<ComponentFile>> getAllComponentsFiles() {
        return domService.getFileElements(ComponentFile.class, project, allScope(project))
                .findAll { !isInTestDir(it) }
    }

    @Override
    List<String> getAllComponentsNames() {
        return domService.getFileElements(ComponentFile.class, project, allScope(project))
                .findAll { !isInTestDir(it) }
                .collect { it.getRootElement() }
                .collect { it.name.value }
    }

    PsiFile getPsiFileAtLocation(String componentPathToFile) {
        if (!componentPathToFile) return null
        Matcher componentMatcher = ComponentAwareFileReferenceSet.COMPONENT_NAME_PATTERN.matcher(componentPathToFile)

        if (componentMatcher.find() && componentMatcher.groupCount() != 0) {
            List<String> pathPieces = FileHandlingUtils.splitPathToList(componentPathToFile)

            PsiDirectory currentDir = getComponentDir(pathPieces.first())
            try {
                for (int i = 1; i < pathPieces.size() - 1; i++) {
                    currentDir = currentDir.findSubdirectory(pathPieces.get(i))
                }
            } catch (NullPointerException ignored) {
                return null
            }
            PsiFile file = currentDir.findFile(pathPieces.last())
            return file
        }
        return null
    }

    List<DomElement> getDomElementListFromFileAtLocation(String componentPathToFile, Class fileType,
                                                         String wantedElement) {
        PsiFile psiFile = getPsiFileAtLocation(componentPathToFile)
        DomElement domFile
        switch (fileType) {
            case MenuFile.class:
                domFile = domManager.getFileElement(psiFile as XmlFile, MenuFile.class)
                if (domFile) return domFile.getRootElement().menus
                domFile = domManager.getFileElement(psiFile as XmlFile, CompoundFile.class)
                return domFile.getRootElement().menus.menus
            case ScreenFile.class:
                domFile = domManager.getFileElement(psiFile as XmlFile, ScreenFile.class)
                if (domFile) return domFile.getRootElement().screens
                domFile = domManager.getFileElement(psiFile as XmlFile, CompoundFile.class)
                return domFile.getRootElement().screens.screens
            case FormFile.class:
                domFile = domManager.getFileElement(psiFile as XmlFile, FormFile.class)
                if (domFile) return domFile.getRootElement().forms
                domFile = domManager.getFileElement(psiFile as XmlFile, CompoundFile.class)
                if (wantedElement == 'GRID') {
                    return domFile.getRootElement().forms.grids
                }
                return domFile.getRootElement().forms.forms
        }
        return null
    }

    List<Screen> getScreenListFromFileAtLocation(String componentPathToFile) {
        return getDomElementListFromFileAtLocation(componentPathToFile, ScreenFile.class, null)
    }

    List<Form> getFormListFromFileAtLocation(String componentPathToFile) {
        return getDomElementListFromFileAtLocation(componentPathToFile, FormFile.class, null)
    }

    List<Grid> getGridListFromFileAtLocation(String componentPathToFile) {
        return getDomElementListFromFileAtLocation(componentPathToFile, FormFile.class, 'GRID')
    }

    List<Menu> getMenuListFromFileAtLocation(String componentPathToFile) {
        return getDomElementListFromFileAtLocation(componentPathToFile, MenuFile.class, null)
    }

    Screen getScreenFromFileAtLocation(String componentPathToFile, String screenName) {
        List<Screen> screens = getScreenListFromFileAtLocation(componentPathToFile)
        return screens.find { it.getName().getValue() == screenName }
    }

    Screen getScreenFromPsiFile(PsiFile file, String screenName) {
        DomFileElement<ScreenFile> domFile = domManager.getFileElement(file as XmlFile, ScreenFile.class)
        return domFile.getRootElement().getScreens().find { it.getName().getValue() == screenName }
    }

    Form getFormFromFileAtLocation(String componentPathToFile, String formName) {
        List<Form> forms = getFormListFromFileAtLocation(componentPathToFile)
        return forms.find { it.getName().getValue() == formName }
    }

    Form getFormFromPsiFile(PsiFile file, String formName) {
        DomFileElement<FormFile> domFile = domManager.getFileElement(file as XmlFile, FormFile.class)
        return domFile.getRootElement().getForms().find { it.getName().getValue() == formName }
    }

    Grid getGridFromFileAtLocation(String componentPathToFile, String formName) {
        List<Grid> grids = getGridListFromFileAtLocation(componentPathToFile)
        return grids.find { it.getName().getValue() == formName }
    }

    Grid getGridFromPsiFile(PsiFile file, String formName) {
        DomFileElement<FormFile> domFile = domManager.getFileElement(file as XmlFile, FormFile.class)
        return domFile.getRootElement().getGrids().find { it.getName().getValue() == formName }
    }

    Menu getMenuFromFileAtLocation(String componentPathToFile, String menuName) {
        List<Menu> menus = getMenuListFromFileAtLocation(componentPathToFile)
        return menus.find { it.getName().getValue() == menuName }
    }

    Menu getMenuFromPsiFile(PsiFile file, String menuName) {
        DomFileElement<MenuFile> domFile = domManager.getFileElement(file as XmlFile, MenuFile.class)
        return domFile.getRootElement().getMenus().find { it.getName().getValue() == menuName }
    }

    List<RequestMap> getComponentRequestMaps(String componentName, Project project) {
        PsiDirectory compoDir = getComponentDir(componentName)
        List controllerFiles = domService.getFileElements(
                ControllerFile.class, project,
                directoryScope(compoDir, true))
        if (!controllerFiles) return null
        List<RequestMap> controllerRequests = []
        for (DomFileElement<ControllerFile> controllerFile in controllerFiles) {
            controllerRequests.addAll(controllerFile.getRootElement().getRequestMaps())
            List<Include> includes = controllerFile.getRootElement().getIncludes()
            if (includes) {
                for (Include include in includes) {
                    XmlFile file = getPsiFileAtLocation(include.getLocation().getValue()) as XmlFile
                    if (domManager.getFileElement(file, ControllerFile.class)) {
                        controllerRequests.addAll(domManager.getFileElement(file, ControllerFile.class).getRootElement().getRequestMaps())
                    }
                }
            }
        }
        List cpdFiles = domService.getFileElements(
                CompoundFile.class, project,
                directoryScope(compoDir, true))
        if (cpdFiles) {
            cpdFiles.forEach { DomFileElement<CompoundFile> cpdFile ->
                controllerRequests.addAll(cpdFile.rootElement.siteConf.requestMaps)
            }
        }
        return controllerRequests
    }

    Map<String, List<String>> getAllMountPointsAndRequestMaps(PsiElement myElement) {
        List<Webapp> allWebapps = getAllComponentsFiles()
                .collect { DomFileElement<ComponentFile> cptf -> cptf.rootElement.webapps }
                .flatten() as List<Webapp>

        Map<String, List<String>> result = [:]
        allWebapps.forEach { Webapp webapp ->
            try {
                String mountPoint = webapp.getMountPoint().getValue()
                List<String> webappUris = getWebappMountPointUris(webapp, myElement)
                result.put(mountPoint, webappUris)
            } catch (NullPointerException e) {
                LOG.error(e)
                return []
            }
        }
        return result
    }

    private List getWebappMountPointUris(Webapp webapp, PsiElement myElement) {
        String componentName = MiscUtils.getComponentName(webapp)
        String location = webapp.getLocation().getValue() //
        PsiDirectory directoryToSearch = getComponentDir(componentName)
        String webappDirName = location.substring(location.indexOf('/') + 1, location.length())
        directoryToSearch = directoryToSearch
                .findSubdirectory('webapp')
                .findSubdirectory(webappDirName)
                .findSubdirectory('WEB-INF')

        List controllerFiles = domService.getFileElements(
                ControllerFile.class,
                myElement.project,
                directoryScope(directoryToSearch, true))
        List requestsUris = []
        controllerFiles.each { controllerFile ->
            List<RequestMap> requests = []
            requests.addAll(controllerFile.getRootElement().getRequestMaps())
            requests.addAll(getRequestsFromImports(controllerFile.getRootElement()))
            requests.each { RequestMap request ->
                requestsUris << request.uri.value
            }
        }
        return requestsUris
    }

    List<RequestMap> getRequestsFromImports(ControllerFile controllerFile) {
        List result = []
        if (!controllerFile.getIncludes()) return []
        for (Include include in controllerFile.getIncludes()) {
            String includeLocation = include.getLocation().getValue()
            XmlFile file = getPsiFileAtLocation(includeLocation) as XmlFile
            if (domManager.getFileElement(file, ControllerFile.class)) {
                result.addAll((domManager.getFileElement(file, ControllerFile.class)).rootElement.requestMaps)
            } else if (domManager.getFileElement(file, CompoundFile.class)) {
                result.addAll((domManager.getFileElement(file, CompoundFile.class)).rootElement.siteConf?.requestMaps)
            }
        }
        return result
    }

    List<EntityRelation> getAllEntityRelations() {
        List<EntityRelation> result = []
        getAllEntities().each { Entity entity ->
            result.addAll(entity.getRelations())
        }
        return result
    }


    List<UiLabelFile> getAllUiLabelFiles() {
        return domService.getFileElements(UiLabelFile.class, project, allScope(project))
                .collect { it.getRootElement() }
    }

    List<UiLabelFile> getAllUiLabelFilesInComponent(String componentName) {
        return domService
                .getFileElements(UiLabelFile.class, project, directoryScope(getComponentDir(componentName), true))
                .collect { it.getRootElement() }
    }
}
