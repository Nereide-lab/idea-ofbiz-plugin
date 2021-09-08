package org.apache.ofbiz.base.project;


import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;
import com.intellij.util.xml.DomService;
import org.apache.ofbiz.base.util.FileUtil;
import org.apache.ofbiz.xml.dom.ControllerFile;
import org.apache.ofbiz.xml.dom.ControllerFile.RequestMap;

import java.io.IOException;
import java.util.LinkedList;
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
        List<DomFileElement<ControllerFile>> toto = DomService.getInstance().getFileElements(ControllerFile.class, this.project, GlobalSearchScope.allScope(this.project));
        return toto;
        /*
        List<DomFileElement<ControllerFile>> files = new LinkedList<>();
        try {
            // TODO mettre les fichiers en cache
            List<VirtualFile> controllerFiles = FileUtil.findXmlFiles(this.project.getBasePath(), null, "site-conf",  null);
            if (!controllerFiles.isEmpty()) {
                DomManager manager = DomManager.getDomManager(this.project);
                for (VirtualFile controllerFile : controllerFiles) {
                    XmlFile xmlFile = (XmlFile) PsiManager.getInstance(this.project).findFile(controllerFile);
                    DomFileElement<ControllerFile> file = manager.getFileElement(xmlFile, ControllerFile.class);
                    files.add(file);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
         */
    }

}
