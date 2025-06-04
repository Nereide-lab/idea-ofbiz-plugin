package fr.nereide.dom.element.eca

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xmlb.annotations.Attribute

interface Action extends DomElement {

    @Attribute('service')
    GenericAttributeValue<String> getService()

    @Attribute('mode')
    GenericAttributeValue<String> getMode()

    @Attribute('persist')
    GenericAttributeValue<String> getPersist()
}