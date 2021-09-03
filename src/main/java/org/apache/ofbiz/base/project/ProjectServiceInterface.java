package org.apache.ofbiz.base.project;


import org.apache.ofbiz.xml.dom.ControllerFile.RequestMap;

public interface ProjectServiceInterface {

    RequestMap getControllerUri(String name);

}
