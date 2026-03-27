/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * 'License') you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * 'AS IS' BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package fr.nereide.project

import static com.intellij.psi.search.GlobalSearchScope.allScope
import static com.intellij.psi.search.GlobalSearchScopesCore.directoryScope
import static fr.nereide.project.pattern.OfbizPluginConstants.FILE_AND_ELEMENT_SEPARATOR

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
import fr.nereide.dom.element.entityeca.Eca as Eeca
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
import fr.nereide.dom.element.serviceeca.Eca as Seca
import fr.nereide.dom.element.serviceengine.Engine
import fr.nereide.dom.element.uilabel.Property
import fr.nereide.dom.file.ComponentFile
import fr.nereide.dom.file.CompoundFile
import fr.nereide.dom.file.ControllerFile
import fr.nereide.dom.file.EntityEcaFile
import fr.nereide.dom.file.EntityEngineFile
import fr.nereide.dom.file.EntityModelFile
import fr.nereide.dom.file.FormFile
import fr.nereide.dom.file.MenuFile
import fr.nereide.dom.file.ScreenFile
import fr.nereide.dom.file.ServiceEcaFile
import fr.nereide.dom.file.ServiceEngineFile
import fr.nereide.dom.file.ServiceFile
import fr.nereide.dom.file.UiLabelFile
import fr.nereide.project.utils.FileHandlingUtils
import fr.nereide.project.utils.MiscUtils
import fr.nereide.reference.common.ComponentAwareFileReferenceSet

import java.util.regex.Matcher

/**
 * Main helper class for the plugin.
 * Lots of helper methods for collecting dom elements.
 * Makes heavy use of DomManager and Dom service.
 */
@com.intellij.openapi.components.Service(com.intellij.openapi.components.Service.Level.PROJECT)
final class OfbizProjectHelper {

    private static final Logger LOG = Logger.getInstance(OfbizProjectHelper)
    public static final String GRID = 'GRID'

    private final Project project
    private final DomManager domManager
    private final DomService domService

    static OfbizProjectHelper getInstance(Project project) {
        return project.getService(OfbizProjectHelper)
    }

    OfbizProjectHelper(Project project) {
        this.project = project
        this.domManager = DomManager.getDomManager(project)
        this.domService = DomService.instance
    }

    Project getProject() { return this.project }

    DomManager getDomManager() { return this.domManager }

    RequestMap getRequestMap(String name) {
        List<ControllerFile> relevantDomBlocs = collectAllControllerFiles()
        return relevantDomBlocs*.requestMaps
                .flatten()
                .find { RequestMap req -> req.uri?.value == name } as RequestMap
    }

    ViewMap getViewMap(String name) {
        List<ControllerFile> relevantDomBlocs = collectAllControllerFiles()
        return relevantDomBlocs*.viewMaps
                .flatten()
                .find { ViewMap vm -> vm.name?.value == name } as ViewMap
    }

    List<ControllerFile> collectAllControllerFiles() {
        return domService.getFileElements(ControllerFile, project, allScope(project))*.rootElement +
                domService.getFileElements(CompoundFile, project, allScope(project))*.rootElement.siteConf
    }

    Entity getEntity(String name) {
        return collectAllEntities().find { Entity e -> e.entityName.value == name }
    }

    List<Entity> getEntities(String name) {
        return collectAllEntities().findAll { Entity e -> e.entityName.value == name }
    }

    boolean entityOrViewExists(String name) {
        return (collectAllEntities().any { Entity e -> e.entityName.value == name } ||
                collectAllViewEntities().any { ViewEntity e -> e.entityName.value == name })
    }

    List<Entity> collectAllEntities() {
        return domService.getFileElements(EntityModelFile, project, allScope(project))
                .collect { DomFileElement<EntityModelFile> emf -> emf.rootElement.entities }
                .flatten() as List<Entity>
    }

    ViewEntity getViewEntity(String name) {
        return collectAllViewEntities().find { ViewEntity e -> e.entityName.value == name }
    }

    List<ViewEntity> getViewEntities(String name) {
        return collectAllViewEntities().findAll { ViewEntity e -> e.entityName.value == name }
    }

    List<ViewEntity> collectAllViewEntities() {
        return domService.getFileElements(EntityModelFile, project, allScope(project))
                .collect { DomFileElement<EntityModelFile> emf -> emf.rootElement.viewEntities }
                .flatten() as List<ViewEntity>
    }

    Service getService(String name) {
        return collectAllServices().find { Service s -> s.name.value == name } as Service
    }

    List<Service> getServices(String name) {
        return collectAllServices().findAll { Service s -> s.name.value == name } as List<Service>
    }

    List<Service> collectAllServices() {
        return domService.getFileElements(ServiceFile, project, allScope(project))
                .collect { DomFileElement<ServiceFile> sdf -> sdf.rootElement.services }
                .flatten() as List<Service>
    }

    Property getProperty(String name) {
        return domService.getFileElements(UiLabelFile, project, allScope(project))
                .collect { DomFileElement<UiLabelFile> ulf -> ulf.rootElement.properties }
                .flatten()
                .find { Property p -> p.key.value == name } as Property
    }

    Datasource getDatasource(String name) {
        return domService.getFileElements(EntityEngineFile, project, allScope(project))
                .collect { DomFileElement<EntityEngineFile> eef -> eef.rootElement.datasources }
                .flatten()
                .find { Datasource d -> d.name.value == name } as Datasource
    }

    List<Form> collectAllFormsFromCurrentFileFromElement(XmlElement myVal) {
        DomFileElement<FormFile> formFile = domManager.getFileElement(myVal.containingFile as XmlFile, FormFile)
        if (formFile) return formFile.rootElement.forms
        return domManager.getFileElement(myVal.containingFile as XmlFile, CompoundFile).rootElement.forms.forms
    }

    List<Screen> collectAllScreenFromCurrentFileFromElement(XmlElement myVal) {
        DomFileElement<ScreenFile> screenFile = domManager.getFileElement(myVal.containingFile as XmlFile, ScreenFile)
        if (screenFile) return screenFile.rootElement.screens
        return domManager.getFileElement(myVal.containingFile as XmlFile, CompoundFile).rootElement.screens.screens
    }

    List<Menu> collectAllMenuFromCurrentFileFromElement(XmlElement myVal) {
        DomFileElement<MenuFile> menuFile = domManager.getFileElement(myVal.containingFile as XmlFile, MenuFile)
        if (menuFile) return menuFile.rootElement.menus
        return domManager.getFileElement(myVal.containingFile as XmlFile, CompoundFile).rootElement.menus.menus
    }

    List<Screen> getScreensFromScreenFileAtLocation(XmlElement screenLocation, boolean isController) {
        String location
        if (isController) {
            String locationText = screenLocation.text
            location = locationText.substring(0, locationText.lastIndexOf(FILE_AND_ELEMENT_SEPARATOR))
        } else {
            location = (screenLocation as XmlAttributeValue).value
        }
        PsiFile file = getPsiFileAtLocation(location)
        DomFileElement<ScreenFile> screenFile = domManager.getFileElement(file as XmlFile, ScreenFile)
        if (screenFile) {
            return screenFile.rootElement.screens
        }
        return domManager.getFileElement(file as XmlFile, CompoundFile).rootElement.screens.screens
    }

    PsiDirectory getComponentDir(String name) {
        List<DomFileElement<ComponentFile>> componentFiles = collectAllComponentsFiles()
        DomFileElement<ComponentFile> relevantComponent = componentFiles
                .find { DomFileElement<ComponentFile> componentFile ->
                    componentFile.rootElement.name.value.equalsIgnoreCase(name)
                }
        if (relevantComponent) {
            return relevantComponent.file.containingDirectory
        }
        return null
    }

    List<ExtendEntity> getExtendEntityListForEntity(PsiElement element) {
        return getExtendEntityListForEntity(MiscUtils.getStringValueFromPsiElement(element))
    }

    List<ExtendEntity> getExtendEntityListForEntity(String entityName) {
        return domService.getFileElements(EntityModelFile, project, allScope(project))
                .collect { DomFileElement<EntityModelFile> emf -> emf.rootElement.extendEntities }
                .flatten()
                .findAll { ExtendEntity ee -> ee.entityName.value == entityName } as List<ExtendEntity>
    }

    List<DomFileElement<ComponentFile>> collectAllComponentsFiles() {
        return domService.getFileElements(ComponentFile, project, allScope(project))
                .findAll { compoFile -> !MiscUtils.isInTestDir(compoFile) }
    }

    List<String> collectAllComponentsNames() {
        return domService.getFileElements(ComponentFile, project, allScope(project))
                .findAll { compoFile -> !MiscUtils.isInTestDir(compoFile) }*.rootElement*.name*.value
    }

    PsiFile getPsiFileAtLocation(String componentPathToFile) {
        if (!componentPathToFile) return null
        Matcher componentMatcher = ComponentAwareFileReferenceSet.COMPONENT_NAME_PATTERN.matcher(componentPathToFile)

        if (componentMatcher.find() && componentMatcher.groupCount() != 0) {
            List<String> pathPieces = FileHandlingUtils.splitPathToList(componentPathToFile)

            PsiDirectory currentDir = getComponentDir(pathPieces.first())
            for (int i = 1; i < pathPieces.size() - 1; i++) {
                if (!currentDir) return null
                currentDir = currentDir.findSubdirectory(pathPieces.get(i))
            }
            PsiFile file = currentDir.findFile(pathPieces.last())
            return file
        }
        return null
    }

    List<DomElement> getDomElementListFromFileAtLocation(String componentPathToFile, Class fileType) {
        return getDomElementListFromFileAtLocation(componentPathToFile, fileType, null)
    }

    List<DomElement> getDomElementListFromFileAtLocation(String componentPathToFile, Class fileType,
                                                         String wantedElement) {
        PsiFile psiFile = getPsiFileAtLocation(componentPathToFile)
        DomElement domFile
        switch (fileType) {
            case MenuFile:
                domFile = domManager.getFileElement(psiFile as XmlFile, MenuFile)
                if (domFile) return domFile.rootElement.menus
                domFile = domManager.getFileElement(psiFile as XmlFile, CompoundFile)
                return domFile.rootElement.menus.menus
            case ScreenFile:
                domFile = domManager.getFileElement(psiFile as XmlFile, ScreenFile)
                if (domFile) return domFile.rootElement.screens
                domFile = domManager.getFileElement(psiFile as XmlFile, CompoundFile)
                return domFile.rootElement.screens.screens
            case FormFile:
                domFile = domManager.getFileElement(psiFile as XmlFile, FormFile)
                if (wantedElement == GRID) {
                    if (domFile) return domFile.rootElement.grids
                    domFile = domManager.getFileElement(psiFile as XmlFile, CompoundFile)
                    return domFile.rootElement.forms.grids
                }
                if (domFile) return domFile.rootElement.forms
                domFile = domManager.getFileElement(psiFile as XmlFile, CompoundFile)
                return domFile.rootElement.forms.forms
        }
        return Collections.emptyList()
    }

    Screen getScreenFromFileAtLocation(String componentPathToFile, String screenName) {
        List<Screen> screens = getDomElementListFromFileAtLocation(componentPathToFile, ScreenFile)
        return screens.find { screen -> screen.name.value == screenName }
    }

    Screen getScreenFromPsiFile(PsiFile file, String screenName) {
        DomFileElement<ScreenFile> domFile = domManager.getFileElement(file as XmlFile, ScreenFile)
        return domFile.rootElement.screens.find { screen -> screen.name.value == screenName }
    }

    Form getFormFromFileAtLocation(String componentPathToFile, String formName) {
        List<Form> forms = getDomElementListFromFileAtLocation(componentPathToFile, FormFile)
        return forms.find { form -> form.name.value == formName }
    }

    Form getFormFromPsiFile(PsiFile file, String formName) {
        DomFileElement<FormFile> domFile = domManager.getFileElement(file as XmlFile, FormFile)
        return domFile.rootElement.forms.find { form -> form.name.value == formName }
    }

    Grid getGridFromFileAtLocation(String componentPathToFile, String formName) {
        List<Grid> grids = getDomElementListFromFileAtLocation(componentPathToFile, FormFile, GRID)
        return grids.find { grid -> grid.name.value == formName }
    }

    Grid getGridFromPsiFile(PsiFile file, String formName) {
        DomFileElement<FormFile> domFile = domManager.getFileElement(file as XmlFile, FormFile)
        return domFile.rootElement.grids.find { grid -> grid.name.value == formName }
    }

    Menu getMenuFromFileAtLocation(String componentPathToFile, String menuName) {
        List<Menu> menus = getDomElementListFromFileAtLocation(componentPathToFile, MenuFile)
        return menus.find { menu -> menu.name.value == menuName }
    }

    Menu getMenuFromPsiFile(PsiFile file, String menuName) {
        DomFileElement<MenuFile> domFile = domManager.getFileElement(file as XmlFile, MenuFile)
        return domFile.rootElement.menus.find { menu -> menu.name.value == menuName }
    }

    /**
     * @return a map with the following format <br>
     * <code>['mount-point' : [@RequestMap, @RequestMap, .. ]] </code>
     */
    Map<String, Set<RequestMap>> getStructuredComponentRequestMaps(String componentName) {
        PsiDirectory compoDir = getComponentDir(componentName)
        Map<String, Set<RequestMap>> componentRequests = [:]

        List<DomFileElement<ComponentFile>> componentFiles = domService.getFileElements(
                ComponentFile, project,
                directoryScope(compoDir, true))
        if (!componentFiles || componentFiles.size() > 1) {
            LOG.error("Multiple or no component file found in component $componentName")
            return Collections.emptyList()
        }
        ComponentFile component = componentFiles.first.rootElement
        List<Webapp> webapps = component.webapps

        for (Webapp webapp : webapps) { // TODO refactor and extract (make use in getWebappMountPointUris
            PsiDirectory currentControllerFolder = getwebappControllerDirectory(webapp, compoDir)
            List<ControllerFile> controllerFiles = domService.getFileElements(
                    ControllerFile, project,
                    directoryScope(currentControllerFolder, true))
            List<ComponentFile> cpdFiles = domService.getFileElements(
                    CompoundFile, project,
                    directoryScope(currentControllerFolder, true))
            String mountPoint = webapp.mountPoint.value
            componentRequests.put(mountPoint, collectRequestMapsFromControllerFiles(controllerFiles, cpdFiles))
        }
        return componentRequests
    }

    PsiDirectory getwebappControllerDirectory(Webapp webapp, PsiDirectory compoDir) {
        String filesLocations = webapp.location.value
        List subFold = filesLocations.split('/').findAll { dir -> dir != 'WEB-INF' }
        PsiDirectory currentControllerFolder = compoDir
        subFold.each { dir ->
            if (!currentControllerFolder) return
            currentControllerFolder = currentControllerFolder.findSubdirectory(dir)
        }
        return currentControllerFolder
    }

    Set<RequestMap> getComponentRequestMaps(String componentName) {
        PsiDirectory compoDir = getComponentDir(componentName)
        List controllerFiles = domService.getFileElements(
                ControllerFile, project,
                directoryScope(compoDir, true))
        List cpdFiles = domService.getFileElements(
                CompoundFile, project,
                directoryScope(compoDir, true))
        return collectRequestMapsFromControllerFiles(controllerFiles, cpdFiles)
    }

    Set<RequestMap> collectRequestMapsFromControllerFiles(List<ControllerFile> controllerFiles,
                                                          List<CompoundFile> compoundFiles) {
        if (!controllerFiles) return Collections.emptyList()
        Set<RequestMap> controllerRequests = []

        for (DomFileElement<ControllerFile> controllerFile : controllerFiles) {
            controllerRequests.addAll(controllerFile.rootElement.requestMaps)
            controllerRequests.addAll(collectIncludedRequestsIfAny(controllerFile))
        }

        for (DomFileElement<CompoundFile> cpdFile : compoundFiles) {
            controllerRequests.addAll(cpdFile.rootElement.siteConf.requestMaps)
        }
        return controllerRequests
    }

    Set<RequestMap> collectIncludedRequestsIfAny(DomFileElement<ControllerFile> controllerFile) {
        Set<RequestMap> result = []
        List<Include> includes = controllerFile.rootElement.includes
        if (!includes) {
            return result
        }
        for (Include include : includes) {
            XmlFile file = getPsiFileAtLocation(include.location.value) as XmlFile
            if (domManager.getFileElement(file, ControllerFile)) {
                result.addAll(domManager.getFileElement(file, ControllerFile).rootElement.requestMaps)
            }
        }
        return result
    }

    Set<String> getMountPointsOfUri(XmlAttributeValue myUriAttr) {
        Set<String> result = []
        collectAllWebappsInProject().forEach { Webapp webapp ->
            List<String> uris = getWebappMountPointUris(webapp)
            if (uris && uris.contains(myUriAttr.value)) {
                result << webapp.mountPoint.value
            }
        }
        return result
    }

    Map<String, List<String>> collectAllMountPointsAndRequestMaps() {
        Map<String, List<String>> result = [:]
        collectAllWebappsInProject().forEach { Webapp webapp ->
            String mountPoint = webapp.mountPoint.value
            List<String> webappUris = getWebappMountPointUris(webapp)
            result.put(mountPoint, webappUris)
        }
        return result
    }

    List<Webapp> collectAllWebappsInProject() {
        return collectAllComponentsFiles()
                .collect { DomFileElement<ComponentFile> cptf -> cptf.rootElement.webapps }
                .flatten() as List<Webapp>
    }

    List<String> getWebappMountPointUris(Webapp webapp) {
        String componentName = MiscUtils.getComponentName(webapp)
        if (componentName.contains('@')) return Collections.emptyList()
        PsiDirectory directoryToSearch = getwebappControllerDirectory(webapp, getComponentDir(componentName))
        List controllerFiles = domService.getFileElements(
                ControllerFile,
                project,
                directoryScope(directoryToSearch, true))
        List requestsUris = []
        controllerFiles.each { controllerFile ->
            List<RequestMap> requests = []
            requests.addAll(controllerFile.rootElement.requestMaps)
            requests.addAll(getRequestsFromImports(controllerFile.rootElement))
            requests.each { RequestMap request ->
                requestsUris << request.uri.value
            }
        }
        return requestsUris.flatten()
    }

    List<RequestMap> getRequestsFromImports(ControllerFile controllerFile) {
        List result = []
        if (!controllerFile.includes) return []
        for (Include include in controllerFile.includes) {
            String includeLocation = include.location.value
            XmlFile file = getPsiFileAtLocation(includeLocation) as XmlFile
            if (domManager.getFileElement(file, ControllerFile)) {
                result.addAll((domManager.getFileElement(file, ControllerFile)).rootElement.requestMaps)
            } else if (domManager.getFileElement(file, CompoundFile)) {
                result.addAll((domManager.getFileElement(file, CompoundFile)).rootElement.siteConf?.requestMaps)
            }
        }
        return result
    }

    List<EntityRelation> collectAllEntityRelations() {
        List<EntityRelation> result = []
        collectAllEntities().each { Entity entity ->
            result.addAll(entity.relations)
        }
        return result
    }

    List<UiLabelFile> collectAllUiLabelFiles() {
        return domService.getFileElements(UiLabelFile, project, allScope(project))*.rootElement
    }

    List<UiLabelFile> collectAllUiLabelFilesInComponent(String componentName) {
        return domService
                .getFileElements(UiLabelFile, project, directoryScope(getComponentDir(componentName), true))
                *.rootElement
    }

    List<Seca> getEcasForService(PsiElement element) {
        String serviceName = MiscUtils.getStringValueFromPsiElement(element)
        if (getService(serviceName) == null) {
            return []
        }
        return domService.getFileElements(ServiceEcaFile, project, allScope(project))
                .collect { ecaFile -> ecaFile.rootElement.ecas }
                .flatten()
                .findAll { Seca eca ->
                    eca.service && eca.service.value == serviceName
                } as List<Seca>
    }

    List<Eeca> getEcasForEntity(PsiElement element) {
        String entityName = MiscUtils.getStringValueFromPsiElement(element)
        if (getEntity(entityName) == null) {
            return []
        }
        return domService.getFileElements(EntityEcaFile, project, allScope(project))
                .collect { ecaFile -> ecaFile.rootElement.ecas }
                .flatten()
                .findAll { Eeca eca ->
                    eca.entity && eca.entity.value == entityName
                } as List<Eeca>
    }

    Engine getEngine(String name) {
        return domService.getFileElements(ServiceEngineFile, project, allScope(project))
                .collect { engineFile -> engineFile.rootElement.serviceEngines.engines }
                .flatten()
                .find { Engine engine ->
                    engine.name.value == name
                }
    }

}
