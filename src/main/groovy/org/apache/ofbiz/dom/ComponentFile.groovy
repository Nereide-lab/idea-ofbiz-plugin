package org.apache.ofbiz.dom

import com.intellij.util.xml.Attribute
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.Stubbed
import com.intellij.util.xml.SubTagList

interface ComponentFile extends DomElement {
    @Attribute("name")
    @Stubbed
    GenericAttributeValue<String> getName();

    @SubTagList("entity-resource")
    List<EntityModelFile> getEntityResources();

    @SubTagList("service-resource")
    List<ServiceDefFile> getServiceResources();

}
