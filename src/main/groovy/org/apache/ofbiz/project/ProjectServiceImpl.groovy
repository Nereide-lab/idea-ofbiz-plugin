package org.apache.ofbiz.project

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiMethod
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileElement
import com.intellij.util.xml.DomService
import org.apache.ofbiz.dom.ComponentFile
import org.apache.ofbiz.dom.ControllerFile
import org.apache.ofbiz.dom.ControllerFile.ViewMap
import org.apache.ofbiz.dom.ControllerFile.RequestMap
import org.apache.ofbiz.dom.EntityModelFile
import org.apache.ofbiz.dom.EntityModelFile.Entity
import org.apache.ofbiz.dom.EntityModelFile.ViewEntity
import org.apache.ofbiz.dom.FormFile
import org.apache.ofbiz.dom.FormFile.Form
import org.apache.ofbiz.dom.FormFile.Grid
import org.apache.ofbiz.dom.MenuFile
import org.apache.ofbiz.dom.MenuFile.Menu
import org.apache.ofbiz.dom.ScreenFile
import org.apache.ofbiz.dom.ScreenFile.Screen
import org.apache.ofbiz.dom.ServiceDefFile
import org.apache.ofbiz.dom.ServiceDefFile.Service
import org.apache.ofbiz.dom.UiLabelFile
import org.apache.ofbiz.dom.UiLabelFile.Property


class ProjectServiceImpl implements ProjectServiceInterface {
    private final Project project

    ProjectServiceImpl(Project project) {
        this.project = project
    }

    RequestMap getControllerUri(String name) {
        return getMatchingElementFromXmlFiles(ControllerFile.class, "getRequestMap", "getUri", name)
    }

    ViewMap getControllerViewName(String name) {
        return getMatchingElementFromXmlFiles(ControllerFile.class, "getViewMap", "getName", name)
    }

    Entity getEntity(String name) {
        return getMatchingElementFromXmlFiles(EntityModelFile.class, "getEntities", "getEntityName", name)
    }

    ViewEntity getViewEntity(String name) {
        return getMatchingElementFromXmlFiles(EntityModelFile.class, "getViewEntities", "getEntityName", name)
    }

    Service getService(String name) {
        return getMatchingElementFromXmlFiles(ServiceDefFile.class, "getServices", "getName", name)
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

    Menu getMenu(String name) {
        return getMatchingElementFromXmlFiles(MenuFile.class, "getMenus", "getName", name)
    }

    PsiDirectory getComponentDir(String name) {
        List<DomFileElement> componentFiles = DomService.getInstance()
                .getFileElements(ComponentFile.class, project, GlobalSearchScope.allScope(project))

        for (DomFileElement component : componentFiles) {
            if (component.getRootElement().getName().getValue().equalsIgnoreCase(name)) {
                component = (DomFileElement) component
                return component.getFile().getContainingDirectory()
            }
        }
        return null
    }

    PsiMethod getMethod(String name) {
        List<DomFileElement> componentFiles = DomService.getInstance()
                .getFileElements(ComponentFile.class, project, GlobalSearchScope.allScope(project))

        for (DomFileElement component : componentFiles) {
            if (component.getRootElement().getName().getValue().equalsIgnoreCase(name)) {
                component = (DomFileElement) component
                return component.getFile().getContainingDirectory()
            }
        }
        return null
    }



    private DomElement getMatchingElementFromXmlFiles(Class classFile,
                                                      String fileElementGetterName,
                                                      String elementValueGetterName,
                                                      String matchingValue) {
        List<DomFileElement<?>> projectFiles = getClassMatchingProjectFiles(classFile, this.project)
        for (DomFileElement<?> projectFile : projectFiles) {
            List<DomElement> elements = projectFile.getRootElement()."$fileElementGetterName"()
            for (DomElement element : elements) {
                if (element."$elementValueGetterName"().getValue().equalsIgnoreCase(matchingValue)) return element
            }
        }
        return null
    }

    private static List<DomFileElement<?>> getClassMatchingProjectFiles(Class classFile, Project project) {
        return DomService.getInstance().getFileElements(classFile, project, GlobalSearchScope.allScope(project))
    }

}
