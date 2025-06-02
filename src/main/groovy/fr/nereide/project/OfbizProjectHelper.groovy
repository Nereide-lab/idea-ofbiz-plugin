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
import com.intellij.psi.PsiLiteralExpression
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
import fr.nereide.dom.element.eca.Eca
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
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral

import java.util.regex.Matcher

import static com.intellij.psi.search.GlobalSearchScope.allScope
import static com.intellij.psi.search.GlobalSearchScopesCore.directoryScope

@com.intellij.openapi.components.Service(com.intellij.openapi.components.Service.Level.PROJECT)
final class OfbizProjectHelper {
    private static final Logger LOG = Logger.getInstance(OfbizProjectHelper.class)

    private final Project project
    private final DomManager domManager
    private final DomService domService

    static OfbizProjectHelper getInstance(Project project) {
        return project.getService(OfbizProjectHelper.class)
    }

    OfbizProjectHelper(Project project) {
        this.project = project
        this.domManager = DomManager.getDomManager(project)
        this.domService = DomService.instance
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

    List<Entity> getEntities(String name) {
        return getAllEntities().findAll() { Entity e -> e.entityName.value == name }
    }

    boolean entityOrViewExists(String name) {
        return (getAllEntities().any() { Entity e -> e.entityName.value == name } ||
                getAllViewEntities().any() { ViewEntity e -> e.entityName.value == name })
    }

    List<Entity> getAllEntities() {
        return domService.getFileElements(EntityModelFile.class, project, allScope(project))
                .collect { DomFileElement<EntityModelFile> emf -> emf.rootElement.entities }
                .flatten() as List<Entity>
    }

    ViewEntity getViewEntity(String name) {
        return getAllViewEntities().find { ViewEntity e -> e.entityName.value == name }
    }

    List<ViewEntity> getViewEntities(String name) {
        return getAllViewEntities().findAll() { ViewEntity e -> e.entityName.value == name }
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

    List<Form> getAllFormsFromCurrentFileFromElement(XmlElement myVal) {
        DomFileElement<FormFile> formFile = domManager.getFileElement(myVal.containingFile as XmlFile, FormFile.class)
        if (formFile) return formFile.rootElement.forms
        return domManager.getFileElement(myVal.containingFile as XmlFile, CompoundFile.class).rootElement.forms.forms
    }

    List<Screen> getAllScreenFromCurrentFileFromElement(XmlElement myVal) {
        DomFileElement<ScreenFile> screenFile = domManager.getFileElement(myVal.containingFile as XmlFile, ScreenFile.class)
        if (screenFile) return screenFile.rootElement.screens
        return domManager.getFileElement(myVal.containingFile as XmlFile, CompoundFile.class).rootElement.screens.screens
    }

    List<Menu> getAllMenuFromCurrentFileFromElement(XmlElement myVal) {
        DomFileElement<MenuFile> menuFile = domManager.getFileElement(myVal.containingFile as XmlFile, MenuFile.class)
        if (menuFile) return menuFile.rootElement.menus
        return domManager.getFileElement(myVal.containingFile as XmlFile, CompoundFile.class).rootElement.menus.menus
    }

    List<Screen> getScreensFromScreenFileAtLocation(XmlElement screenLocation, boolean isController) {
        String location
        if (isController) {
            String locationText = screenLocation.text
            location = locationText.substring(0, locationText.lastIndexOf('#'))
        } else {
            location = (screenLocation as XmlAttributeValue).value
        }
        PsiFile file = getPsiFileAtLocation(location)
        DomFileElement<ScreenFile> screenFile = domManager.getFileElement(file as XmlFile, ScreenFile.class)
        if (screenFile) {
            return screenFile.rootElement.screens
        }
        return domManager.getFileElement(file as XmlFile, CompoundFile.class).rootElement.screens.screens
    }

    static boolean isInTestDir(DomFileElement domFile) {
        String dir = domFile.file.containingDirectory.toString()
        return dir.contains('/tests')
    }

    PsiDirectory getComponentDir(String name) {
        List<DomFileElement<ComponentFile>> componentFiles = getAllComponentsFiles()
        DomFileElement<ComponentFile> relevantComponent = componentFiles
                .find { DomFileElement<ComponentFile> componentFile ->
                    componentFile.rootElement.name.value.equalsIgnoreCase(name)
                }
        if (relevantComponent) {
            return relevantComponent.file.containingDirectory
        }
        return null
    }

    List<ExtendEntity> getExtendEntityListForEntity(String entityName) {
        return domService.getFileElements(EntityModelFile.class, project, allScope(project))
                .collect { DomFileElement<EntityModelFile> emf -> emf.rootElement.extendEntities }
                .flatten()
                .findAll { ExtendEntity ee -> ee.entityName.value == entityName }
                .collect() as List<ExtendEntity>

    }

    List<DomFileElement<ComponentFile>> getAllComponentsFiles() {
        return domService.getFileElements(ComponentFile.class, project, allScope(project))
                .findAll { !isInTestDir(it) }
    }

    List<String> getAllComponentsNames() {
        return domService.getFileElements(ComponentFile.class, project, allScope(project))
                .findAll { !isInTestDir(it) }
                .collect { it.rootElement }
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

    List<DomElement> getDomElementListFromFileAtLocation(String componentPathToFile, Class fileType) {
        return getDomElementListFromFileAtLocation(componentPathToFile, fileType, null)
    }

    List<DomElement> getDomElementListFromFileAtLocation(String componentPathToFile, Class fileType,
                                                         String wantedElement) {
        PsiFile psiFile = getPsiFileAtLocation(componentPathToFile)
        DomElement domFile
        switch (fileType) {
            case MenuFile.class:
                domFile = domManager.getFileElement(psiFile as XmlFile, MenuFile.class)
                if (domFile) return domFile.rootElement.menus
                domFile = domManager.getFileElement(psiFile as XmlFile, CompoundFile.class)
                return domFile.rootElement.menus.menus
            case ScreenFile.class:
                domFile = domManager.getFileElement(psiFile as XmlFile, ScreenFile.class)
                if (domFile) return domFile.rootElement.screens
                domFile = domManager.getFileElement(psiFile as XmlFile, CompoundFile.class)
                return domFile.rootElement.screens.screens
            case FormFile.class:
                domFile = domManager.getFileElement(psiFile as XmlFile, FormFile.class)
                if (wantedElement == 'GRID') {
                    if (domFile) return domFile.rootElement.grids
                    domFile = domManager.getFileElement(psiFile as XmlFile, CompoundFile.class)
                    return domFile.rootElement.forms.grids
                }
                if (domFile) return domFile.rootElement.forms
                domFile = domManager.getFileElement(psiFile as XmlFile, CompoundFile.class)
                return domFile.rootElement.forms.forms
        }
        return null
    }

    Screen getScreenFromFileAtLocation(String componentPathToFile, String screenName) {
        List<Screen> screens = getDomElementListFromFileAtLocation(componentPathToFile, ScreenFile.class)
        return screens.find { it.name.value == screenName }
    }

    Screen getScreenFromPsiFile(PsiFile file, String screenName) {
        DomFileElement<ScreenFile> domFile = domManager.getFileElement(file as XmlFile, ScreenFile.class)
        return domFile.rootElement.screens.find { it.name.value == screenName }
    }

    Form getFormFromFileAtLocation(String componentPathToFile, String formName) {
        List<Form> forms = getDomElementListFromFileAtLocation(componentPathToFile, FormFile.class)
        return forms.find { it.name.value == formName }
    }

    Form getFormFromPsiFile(PsiFile file, String formName) {
        DomFileElement<FormFile> domFile = domManager.getFileElement(file as XmlFile, FormFile.class)
        return domFile.rootElement.forms.find { it.name.value == formName }
    }

    Grid getGridFromFileAtLocation(String componentPathToFile, String formName) {
        List<Grid> grids = getDomElementListFromFileAtLocation(componentPathToFile, FormFile.class, 'GRID')
        return grids.find { it.name.value == formName }
    }

    Grid getGridFromPsiFile(PsiFile file, String formName) {
        DomFileElement<FormFile> domFile = domManager.getFileElement(file as XmlFile, FormFile.class)
        return domFile.rootElement.grids.find { it.name.value == formName }
    }

    Menu getMenuFromFileAtLocation(String componentPathToFile, String menuName) {
        List<Menu> menus = getDomElementListFromFileAtLocation(componentPathToFile, MenuFile.class)
        return menus.find { it.name.value == menuName }
    }

    Menu getMenuFromPsiFile(PsiFile file, String menuName) {
        DomFileElement<MenuFile> domFile = domManager.getFileElement(file as XmlFile, MenuFile.class)
        return domFile.rootElement.menus.find { it.name.value == menuName }
    }

    List<RequestMap> getComponentRequestMaps(String componentName) {
        PsiDirectory compoDir = getComponentDir(componentName)
        List controllerFiles = domService.getFileElements(
                ControllerFile.class, project,
                directoryScope(compoDir, true))
        if (!controllerFiles) return null
        List<RequestMap> controllerRequests = []
        for (DomFileElement<ControllerFile> controllerFile in controllerFiles) {
            controllerRequests.addAll(controllerFile.rootElement.requestMaps)
            List<Include> includes = controllerFile.rootElement.includes
            if (includes) {
                for (Include include in includes) {
                    XmlFile file = getPsiFileAtLocation(include.location.value) as XmlFile
                    if (domManager.getFileElement(file, ControllerFile.class)) {
                        controllerRequests.addAll(domManager.getFileElement(file, ControllerFile.class).rootElement.requestMaps)
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
                String mountPoint = webapp.mountPoint.value
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
        String location = webapp.location.value
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
            requests.addAll(controllerFile.rootElement.requestMaps)
            requests.addAll(getRequestsFromImports(controllerFile.rootElement))
            requests.each { RequestMap request ->
                requestsUris << request.uri.value
            }
        }
        return requestsUris
    }

    List<RequestMap> getRequestsFromImports(ControllerFile controllerFile) {
        List result = []
        if (!controllerFile.getIncludes()) return []
        for (Include include in controllerFile.includes) {
            String includeLocation = include.location.value
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
            result.addAll(entity.relations)
        }
        return result
    }


    List<UiLabelFile> getAllUiLabelFiles() {
        return domService.getFileElements(UiLabelFile.class, project, allScope(project))
                .collect { it.rootElement }
    }

    List<UiLabelFile> getAllUiLabelFilesInComponent(String componentName) {
        return domService
                .getFileElements(UiLabelFile.class, project, directoryScope(getComponentDir(componentName), true))
                .collect { it.rootElement }
    }

    List<Eca> getEcasForService(PsiElement element) {
        String serviceName = element.text
        if (element instanceof XmlAttributeValue) {
            serviceName = (element as XmlAttributeValue).value
        } else if (element instanceof PsiLiteralExpression) {
            serviceName = (element as PsiLiteralExpression).value
        } else if (element instanceof GrLiteral) { // Groovy case
            serviceName = (element as GrLiteral).value
        }

        if (getService(serviceName) == null) {
            return []
        }

        return domService.getFileElements(ServiceEcaFile.class, project, allScope(project))
                .collect { it.rootElement.ecas }
                .flatten()
                .findAll { Eca eca ->
                    eca.service && eca.service.value == serviceName
                } as List<Eca>
    }
}
