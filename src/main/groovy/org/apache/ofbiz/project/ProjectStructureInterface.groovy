package org.apache.ofbiz.project;


import com.intellij.util.xml.DomElement
import org.apache.ofbiz.xml.domelement.ControllerFile;

public interface ProjectStructureInterface {

    ControllerFile.RequestMap getControllerUri(String name);

}
