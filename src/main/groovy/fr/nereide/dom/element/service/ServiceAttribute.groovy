package fr.nereide.dom.element.service

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue
import com.intellij.util.xml.Stubbed
import com.intellij.util.xmlb.annotations.Attribute

interface ServiceAttribute extends DomElement {

    @NameValue
    @Stubbed
    @Attribute("name")
    GenericAttributeValue<String> getName()

    @Attribute("type")
    GenericAttributeValue<String> getType()

    @Attribute("optional")
    GenericAttributeValue<String> getOptional()

    @Attribute("mode")
    GenericAttributeValue<String> getMode()

    @Attribute("default-value")
    GenericAttributeValue<String> getDefaultValue()
}
