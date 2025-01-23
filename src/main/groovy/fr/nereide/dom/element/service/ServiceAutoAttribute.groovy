package fr.nereide.dom.element.service

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xmlb.annotations.Attribute

interface ServiceAutoAttribute extends DomElement {

    @Attribute('mode')
    GenericAttributeValue<String> getMode()

    @Attribute('include')
    GenericAttributeValue<String> getInclude()

    @Attribute('optional')
    GenericAttributeValue<String> getOptional()
}
