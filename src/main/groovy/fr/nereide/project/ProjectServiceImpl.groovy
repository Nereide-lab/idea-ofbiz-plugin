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
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.GlobalSearchScopesCore
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlFile
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileElement
import com.intellij.util.xml.DomManager
import com.intellij.util.xml.DomService
import fr.nereide.dom.*
import fr.nereide.dom.ComponentFile.Webapp
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
import fr.nereide.project.utils.FileHandlingUtils
import fr.nereide.project.utils.MiscUtils
import fr.nereide.reference.common.ComponentAwareFileReferenceSet

import java.util.regex.Matcher

import static com.intellij.psi.search.GlobalSearchScopesCore.DirectoryScope.*
import static java.util.stream.Collectors.toList

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

    List<Form> getAllFormsFromCurrentFileFromElement(XmlAttributeValue myVal) {
        DomManager dm = DomManager.getDomManager(myVal.getProject())
        DomFileElement<FormFile> screenFile = dm.getFileElement(myVal.getContainingFile() as XmlFile, FormFile.class)
        return screenFile.getRootElement().getForms()
    }

    List<Screen> getAllScreenFromCurrentFileFromElement(XmlAttributeValue myVal) {
        DomManager dm = DomManager.getDomManager(myVal.getProject())
        DomFileElement<ScreenFile> screenFile = dm.getFileElement(myVal.getContainingFile() as XmlFile, ScreenFile.class)
        return screenFile.getRootElement().getScreens()
    }

    List<Screen> getScreensFromScreenFileAtLocation(XmlElement screenLocation, boolean isController) {
        DomManager dm = DomManager.getDomManager(screenLocation.getProject())
        String location
        if (isController) {
            String locationText = screenLocation.text
            location = locationText.substring(0, locationText.lastIndexOf('#'))
        } else {
            location = (screenLocation as XmlAttributeValue).getValue()
        }
        PsiFile file = getPsiFileAtLocation(location)
        DomFileElement<ScreenFile> screenFile = dm.getFileElement(file as XmlFile, ScreenFile.class)
        if (screenFile) {
            return screenFile.getRootElement().getScreens()
        }
        return dm.getFileElement(file as XmlFile, CompoundFile.class).getRootElement().getScreens().getScreens()
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

    List<DomElement> getDomElementListFromFileAtLocation(DomManager dm, String componentPathToFile, Class fileType,
                                                         String wantedElement) {
        PsiFile psiFile = getPsiFileAtLocation(componentPathToFile)
        DomElement domFile
        switch (fileType) {
            case MenuFile.class:
                domFile = dm.getFileElement(psiFile as XmlFile, MenuFile.class)
                if (domFile) return domFile.getRootElement().menus
                domFile = dm.getFileElement(psiFile as XmlFile, CompoundFile.class)
                return domFile.getRootElement().menus.menus
            case ScreenFile.class:
                domFile = dm.getFileElement(psiFile as XmlFile, ScreenFile.class)
                if (domFile) return domFile.getRootElement().screens
                domFile = dm.getFileElement(psiFile as XmlFile, CompoundFile.class)
                return domFile.getRootElement().screens.screens
            case FormFile.class:
                domFile = dm.getFileElement(psiFile as XmlFile, FormFile.class)
                if (domFile) return domFile.getRootElement().forms
                domFile = dm.getFileElement(psiFile as XmlFile, CompoundFile.class)
                if (wantedElement == 'GRID') {
                    return domFile.getRootElement().forms.grids
                }
                return domFile.getRootElement().forms.forms
        }
        return null
    }

    List<Screen> getScreenListFromFileAtLocation(DomManager dm, String componentPathToFile) {
        return getDomElementListFromFileAtLocation(dm, componentPathToFile, ScreenFile.class, null)
    }

    List<Form> getFormListFromFileAtLocation(DomManager dm, String componentPathToFile) {
        return getDomElementListFromFileAtLocation(dm, componentPathToFile, FormFile.class, null)
    }

    List<Grid> getGridListFromFileAtLocation(DomManager dm, String componentPathToFile) {
        return getDomElementListFromFileAtLocation(dm, componentPathToFile, FormFile.class, 'GRID')
    }

    List<Menu> getMenuListFromFileAtLocation(DomManager dm, String componentPathToFile) {
        return getDomElementListFromFileAtLocation(dm, componentPathToFile, MenuFile.class, null)
    }

    Screen getScreenFromFileAtLocation(DomManager dm, String componentPathToFile, String screenName) {
        List<Screen> screens = getScreenListFromFileAtLocation(dm, componentPathToFile)
        return screens.find { it.getName().getValue() == screenName }
    }

    Screen getScreenFromPsiFile(DomManager dm, PsiFile file, String screenName) {
        DomFileElement<ScreenFile> domFile = dm.getFileElement(file as XmlFile, ScreenFile.class)
        return domFile.getRootElement().getScreens().find { it.getName().getValue() == screenName }
    }

    Form getFormFromFileAtLocation(DomManager dm, String componentPathToFile, String formName) {
        List<Form> forms = getFormListFromFileAtLocation(dm, componentPathToFile)
        return forms.find { it.getName().getValue() == formName }
    }

    Form getFormFromPsiFile(DomManager dm, PsiFile file, String formName) {
        DomFileElement<FormFile> domFile = dm.getFileElement(file as XmlFile, FormFile.class)
        return domFile.getRootElement().getForms().find { it.getName().getValue() == formName }
    }

    Grid getGridFromFileAtLocation(DomManager dm, String componentPathToFile, String formName) {
        List<Grid> grids = getGridListFromFileAtLocation(dm, componentPathToFile)
        return grids.find { it.getName().getValue() == formName }
    }

    Grid getGridFromPsiFile(DomManager dm, PsiFile file, String formName) {
        DomFileElement<FormFile> domFile = dm.getFileElement(file as XmlFile, FormFile.class)
        return domFile.getRootElement().getGrids().find { it.getName().getValue() == formName }
    }

    Menu getMenuFromFileAtLocation(DomManager dm, String componentPathToFile, String menuName) {
        List<Menu> menus = getMenuListFromFileAtLocation(dm, componentPathToFile)
        return menus.find { it.getName().getValue() == menuName }
    }

    Menu getMenuFromPsiFile(DomManager dm, PsiFile file, String menuName) {
        DomFileElement<MenuFile> domFile = dm.getFileElement(file as XmlFile, MenuFile.class)
        return domFile.getRootElement().getMenus().find { it.getName().getValue() == menuName }
    }

    List<RequestMap> getComponentRequestMaps(String componentName, Project project) {
        List controllerFiles = DomService.getInstance()
                .getFileElements(ControllerFile.class, project, GlobalSearchScope.allScope(project))
        if (!controllerFiles) return null
        controllerFiles = controllerFiles.findAll { DomFileElement<ControllerFile> controllerFile ->
            MiscUtils.getComponentName(controllerFile) == componentName
        }
        List<RequestMap> controllerRequests = []
        // TODO : Use a search scope. Cleaner but i can't make it work now.
        controllerFiles.each { DomFileElement<ControllerFile> controllerFile ->
            controllerRequests.addAll(controllerFile.getRootElement().getRequestMaps())
        }
        return controllerRequests
    }

    Map<String, List<String>> getAllMountPointsAndRequestMaps(PsiElement myElement) {
        List<ComponentFileDescription> allComponents = getAllComponentsFiles()
        List<Webapp> allWebapps = []
        allComponents.each { def compoFile ->
            allWebapps.addAll(compoFile.getRootElement().getWebapps())
        }

        Map result = [:]
        allWebapps.forEach { Webapp webapp ->
            try {
                String mountPoint = webapp.getMountPoint().getValue()
                result.put(mountPoint, getAllWebappMountPointUris(webapp, myElement))
            } catch (NullPointerException ignored) {
                return []
            }
        }
        return result
    }

    private List getAllWebappMountPointUris(Webapp webapp, PsiElement myElement) {
        String componentName = MiscUtils.getComponentName(webapp)
        String location = webapp.getLocation().getValue() //
        PsiDirectory directoryToSearch = getComponentDir(componentName)
        String webappDirName = location.substring(location.indexOf('/') + 1, location.length())
        directoryToSearch = directoryToSearch
                .findSubdirectory('webapp')
                .findSubdirectory(webappDirName)
                .findSubdirectory('WEB-INF')

        List controllerFiles = DomService.getInstance().getFileElements(
                ControllerFile.class,
                myElement.project,
                GlobalSearchScopesCore.directoryScope(directoryToSearch, true))
        List requestsUris = []
        controllerFiles.each { DomFileElement<ControllerFile> controllerFile ->
            List requests = controllerFile.getRootElement().getRequestMaps()
            requests.each { RequestMap request ->
                requestsUris << request.uri.value
            }
        }
        return requestsUris
    }
}
