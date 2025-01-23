package fr.nereide.dom.element.screen

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue
import com.intellij.util.xml.Stubbed
import com.intellij.util.xmlb.annotations.Attribute

interface Screen extends DomElement {
    @NameValue
    @Stubbed
    @Attribute("name")
    GenericAttributeValue<String> getName()
}
