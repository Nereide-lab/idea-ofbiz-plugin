package fr.nereide.dom.element.entitymodel

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xmlb.annotations.Attribute

interface Alias extends DomElement {
    @Attribute("name")
    GenericAttributeValue<String> getName()

    @Attribute("entity-alias")
    GenericAttributeValue<String> getEntityAlias()

    @Attribute("field")
    GenericAttributeValue<String> getField()
}
