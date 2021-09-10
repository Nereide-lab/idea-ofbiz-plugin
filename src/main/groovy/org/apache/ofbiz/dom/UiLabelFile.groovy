package org.apache.ofbiz.dom

import com.intellij.util.xml.*
import com.intellij.util.xmlb.annotations.Attribute

interface UiLabelFile extends DomElement {

    @SubTagList("property")
    List<Property> getProperties()

    interface Property extends DomElement {
        @NameValue
        @Attribute("key")
        GenericAttributeValue<String> getKey()

        @SubTagList("value")
        List<GenericDomValue<String>> getValues()
    }
}
