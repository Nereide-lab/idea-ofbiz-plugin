package org.apache.ofbiz.project

import org.apache.ofbiz.dom.ControllerFile.RequestMap;


interface ProjectServiceInterface {

    RequestMap getControllerUri(String name);

}
