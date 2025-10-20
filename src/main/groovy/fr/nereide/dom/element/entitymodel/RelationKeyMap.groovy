package fr.nereide.dom.element.entitymodel

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xmlb.annotations.Attribute

/**
 * Part of the OFBiz DOM description
 */
interface RelationKeyMap extends DomElement {

    @Attribute('field-name')
    GenericAttributeValue<String> getFieldName()

    @Attribute('rel-field-name')
    GenericAttributeValue<String> getRelFieldName()

}
