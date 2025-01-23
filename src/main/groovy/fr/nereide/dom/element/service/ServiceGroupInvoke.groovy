package fr.nereide.dom.element.service

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue
import com.intellij.util.xml.Stubbed
import com.intellij.util.xmlb.annotations.Attribute

interface ServiceGroupInvoke extends DomElement {

    @NameValue
    @Stubbed
    @Attribute("name")
    GenericAttributeValue<String> getName()

    @Attribute("parameters")
    GenericAttributeValue<String> getParameters()

    @Attribute("result-to-context")
    GenericAttributeValue<String> getResultToContext()

    @Attribute("mode")
    GenericAttributeValue<String> getMode()
}
