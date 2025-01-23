package fr.nereide.dom.element.entitymodel

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xmlb.annotations.Attribute

interface EntityPrimKey extends DomElement {
    @Attribute('field')
    GenericAttributeValue<String> getField()
}
