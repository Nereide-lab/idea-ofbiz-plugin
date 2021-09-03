package org.apache.ofbiz.base.project;


import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomService;
import org.apache.ofbiz.xml.dom.ControllerFile;
import org.apache.ofbiz.xml.dom.ControllerFile.RequestMap;

import java.util.List;


public class ProjectServiceImpl implements ProjectServiceInterface {
    private final Project project;

    public ProjectServiceImpl(Project project) {
        this.project = project;
    }

    public RequestMap getControllerUri(String name) {
        List<DomFileElement<ControllerFile>> controllerFiles = this.getControllerFiles();
        for (DomFileElement<ControllerFile> controllerFile: controllerFiles) {
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
