package org.apache.ofbiz.project

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileElement
import com.intellij.util.xml.DomService
import org.apache.ofbiz.dom.ControllerFile
import org.apache.ofbiz.dom.ControllerFile.RequestMap
import org.apache.ofbiz.dom.EntityModelFile
import org.apache.ofbiz.dom.EntityModelFile.Entity
import org.apache.ofbiz.dom.EntityModelFile.ViewEntity



class ProjectServiceImpl implements ProjectServiceInterface {
    private final Project project

    ProjectServiceImpl(Project project) {
        this.project = project
    }

    RequestMap getControllerUri(String name) {
        return getMatchingElementFromClassFiles(ControllerFile.class, "getRequestMap", "getUri", name);
    }

    Entity getEntity(String name){
        return getMatchingElementFromClassFiles(EntityModelFile.class, "getEntities", "getEntityName", name);
    }

    ViewEntity getViewEntity(String name){
        return getMatchingElementFromClassFiles(EntityModelFile.class, "getViewEntities", "getEntityName", name);
    }

    private DomElement<?> getMatchingElementFromClassFiles(Class classFile,
                                                               String fileElementGetterName,
                                                               String elementValueGetterName,
                                                               String matchingValue ) {
        List<DomFileElement<?>> projectFiles = getClassMatchingProjectFiles(classFile, this.project)
        for (DomFileElement<?> projectFile : projectFiles) {
            List<DomElement> elements = projectFile.getRootElement()."$fileElementGetterName"()
            for (DomElement element : elements) {
                if (element."$elementValueGetterName"().getValue().equalsIgnoreCase(matchingValue)) return element
            }
        }
        return null
    }

    private static List<DomFileElement<?>> getClassMatchingProjectFiles (Class classFile, Project project) {
        return DomService.getInstance().getFileElements(classFile , project, GlobalSearchScope.allScope(project));
    }

}
