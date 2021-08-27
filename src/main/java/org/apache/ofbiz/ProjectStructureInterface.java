package org.apache.ofbiz;


import org.apache.ofbiz.xml.domelement.ControllerFile.RequestMap;

public interface ProjectStructureInterface {

    RequestMap getControllerUri(String name);

}
