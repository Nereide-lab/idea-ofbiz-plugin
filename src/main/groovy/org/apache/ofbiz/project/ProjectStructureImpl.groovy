package org.apache.ofbiz.project;


import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomService;

public class ProjectStructureImpl implements ProjectStructureInterface {
    private final Project project;

    public ProjectStructureImpl(Project project) {
        this.project = project;
    }

    public DomElement getControllerUri(String name) {
        List<DomFileElement<DomElement>> controllerFiles = this.getControllerFiles();
        for (DomFileElement<DomElement> controllerFile: controllerFiles) {
            List<DomElement> requestMaps = controllerFile.getRootElement().getRequestMap();
            for (DomElement request : requestMaps) {
                if (request.getUri().getValue().equalsIgnoreCase(name)) return request;
            }
        }
        return null;
    }

    private List<DomFileElement<DomElement>> getControllerFiles() {
        return DomService.getInstance().getFileElements(DomElement.class, this.project, GlobalSearchScope.allScope(this.project));
    }

}
