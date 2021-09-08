package org.apache.ofbiz.project

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.xml.DomFileElement
import com.intellij.util.xml.DomService
import org.apache.ofbiz.dom.ControllerFile
import org.apache.ofbiz.dom.ControllerFile.RequestMap

class ProjectServiceImpl implements ProjectServiceInterface {
    private final Project project;

    ProjectServiceImpl(Project project) {
        this.project = project;
    }

    RequestMap getControllerUri(String name) {
        List<DomFileElement<ControllerFile>> controllerFiles = this.getControllerFiles();
        for (DomFileElement<ControllerFile> controllerFile : controllerFiles) {
            List<RequestMap> requestMaps = controllerFile.getRootElement().getRequestMap();
            for (RequestMap request : requestMaps) {
                if (request.getUri().getValue().equalsIgnoreCase(name)) return request;
            }
        }
        return null;
    }

    private List<DomFileElement<ControllerFile>> getControllerFiles() {
        return DomService.getInstance().getFileElements(ControllerFile.class, this.project, GlobalSearchScope.allScope(this.project));
    }

}
