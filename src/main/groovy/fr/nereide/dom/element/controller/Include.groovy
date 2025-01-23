package fr.nereide.dom.element.controller

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xmlb.annotations.Attribute

interface Include extends DomElement {
    @Attribute("location")
    GenericAttributeValue<String> getLocation()
}
