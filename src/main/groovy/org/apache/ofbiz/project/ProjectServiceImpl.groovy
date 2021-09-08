package org.apache.ofbiz.project

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
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
        List<DomFileElement<ControllerFile>> controllerFiles = this.getControllerFiles()
        for (DomFileElement<ControllerFile> controllerFile : controllerFiles) {
            List<RequestMap> requestMaps = controllerFile.getRootElement().getRequestMap()
            for (RequestMap request : requestMaps) {
                if (request.getUri().getValue().equalsIgnoreCase(name)) return request
            }
        }
        return null
    }

    Entity getEntity(String name){
        List<DomFileElement<EntityModelFile>> entityFiles = getEntityModelFiles()
        for (DomFileElement<EntityModelFile> entityFile : entityFiles) {
            List<Entity> entities = entityFile.getRootElement().getEntities()
            for (Entity entity : entities) {
                if (entity.getEntityName().getValue().equalsIgnoreCase(name)) return entity
            }
        }
        return null
    }

    ViewEntity getViewEntity(String name){
        List<DomFileElement<EntityModelFile>> entityFiles = getEntityModelFiles()
        for (DomFileElement<EntityModelFile> entityFile : entityFiles) {
            List<ViewEntity> entities = entityFile.getRootElement().getViewEntities()
            for (ViewEntity entity : entities) {
                if (entity.getEntityName().getValue().equalsIgnoreCase(name)) return entity
            }
        }
        return null
    }

    private List<DomFileElement<ControllerFile>> getControllerFiles() {
        return DomService.getInstance().getFileElements(ControllerFile.class, this.project, GlobalSearchScope.allScope(this.project))
    }

    private List<DomFileElement<EntityModelFile>> getEntityModelFiles() {
        return DomService.getInstance().getFileElements(EntityModelFile.class, this.project, GlobalSearchScope.allScope(this.project))
    }

    // TODO : Générifier la récupération d'un type particulier de fichier ?
}
