package org.apache.ofbiz;


import org.apache.ofbiz.xml.dom.ControllerFile.RequestMap;

public interface ProjectStructureInterface {

    RequestMap getControllerUri(String name);

}
