package org.apache.ofbiz.dom

import com.intellij.util.xml.*
import com.intellij.util.xmlb.annotations.Attribute

interface ServiceDefFile extends DomElement {

    @SubTagList("service")
    List<Service> getServices()

    interface Service extends DomElement {

        @NameValue
        @Stubbed
        @Attribute("name")
        GenericAttributeValue<String> getName()

        @Attribute("engine")
        GenericAttributeValue<String> getEngine()

        @Attribute("location")
        GenericAttributeValue<String> getLocation()

        @Attribute("invoke")
        GenericAttributeValue<String> getInvoke()

        @SubTagList("implements")
        List<GenericAttributeValue<String>> getImplements()

        @SubTag("description")
        GenericDomValue<String> getDescription()

        @SubTagList("attribute")
        List<ServiceAttribute> getAttributes()

        @SubTagList("auto-attributes")
        List<ServiceAttribute> getAutoAttributes()
    }

    interface ServiceAttribute extends DomElement {
        @NameValue
        @Stubbed
        @Attribute("name")
        GenericAttributeValue<String> getName()

        @Attribute("type")
        GenericAttributeValue<String> getType()
    }

}