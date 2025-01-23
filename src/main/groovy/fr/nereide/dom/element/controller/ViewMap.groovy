package fr.nereide.dom.element.controller

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue
import com.intellij.util.xmlb.annotations.Attribute

interface ViewMap extends DomElement {
    @NameValue
    @Attribute("name")
    GenericAttributeValue<String> getName()

    @Attribute("type")
    GenericAttributeValue<String> getType()
}
