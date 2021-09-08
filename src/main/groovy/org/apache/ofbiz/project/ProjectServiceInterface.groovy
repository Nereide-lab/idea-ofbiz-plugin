package org.apache.ofbiz.project

import org.apache.ofbiz.dom.ControllerFile.RequestMap
import org.apache.ofbiz.dom.EntityModelFile.Entity
import org.apache.ofbiz.dom.EntityModelFile.ViewEntity

interface ProjectServiceInterface {

    RequestMap getControllerUri(String name)

    Entity getEntity(String name)

    ViewEntity getViewEntity(String name)

}
