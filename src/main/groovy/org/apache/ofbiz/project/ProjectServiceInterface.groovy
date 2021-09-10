package org.apache.ofbiz.project

import org.apache.ofbiz.dom.ControllerFile.RequestMap
import org.apache.ofbiz.dom.EntityModelFile.Entity
import org.apache.ofbiz.dom.EntityModelFile.ViewEntity
import org.apache.ofbiz.dom.ServiceDefFile.Service
import org.apache.ofbiz.dom.UiLabelFile.Property

interface ProjectServiceInterface {

    RequestMap getControllerUri(String name)

    Entity getEntity(String name)

    ViewEntity getViewEntity(String name)

    Service getService(String name)

    Property getProperty(String name)

}
