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

import java.util.function.BiFunction
import java.util.function.Function

class ProjectServiceImpl implements ProjectServiceInterface {
    private final Project project

    ProjectServiceImpl(Project project) {
        this.project = project
    }

    RequestMap getControllerUri(String name) {
        return getMatchinTagFromClassFile(ControllerFile.class, ControllerFile.&getRequestMap, RequestMap.&getUri, name);
    }

    Entity getEntity(String name){
        return getMatchinTagFromClassFile(EntityModelFile.class, EntityModelFile.&getEntities, Entity.&getEntityName, name);
    }

    ViewEntity getViewEntity(String name){
        return getMatchinTagFromClassFile(EntityModelFile.class, EntityModelFile.&getViewEntities, ViewEntity.&getEntityName, name);
    }


    private List<DomFileElement<?>> getMatchinTagFromClassFile(Class classFile,
                                                               Function getFileElementMethod,
                                                               Function getElementValueMethod,
                                                               String matchingValue ) {
        List<DomFileElement<?>> projectFiles = getClassProjectMatchingFiles(classFile, this.project)
        for (DomFileElement<?> projectFile : projectFiles) {
            List<DomElement> elements = getFileElementMethod.apply(projectFile.getRootElement())
            for (DomElement element : elements) {
                if (getElementValueMethod.apply(element).getValue().equalsIgnoreCase(matchingValue)) return element
            }
        }
        return null
    }

    private static List<DomFileElement<?>> getClassProjectMatchingFiles (Class classFile, Project project) {
        return DomService.getInstance().getFileElements( classFile , project, GlobalSearchScope.allScope(project));
    }
    // TODO : Générifier la récupération d'un type particulier de fichier ?
}
