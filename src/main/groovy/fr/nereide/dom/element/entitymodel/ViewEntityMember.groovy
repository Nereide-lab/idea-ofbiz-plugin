package fr.nereide.dom.element.entitymodel

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xmlb.annotations.Attribute

/**
 * Part of the OFBiz DOM description
 */
interface ViewEntityMember extends DomElement {

    @Attribute('entity-alias')
    GenericAttributeValue<String> getEntityAlias()

    @Attribute('entity-name')
    GenericAttributeValue<String> getEntityName()

}
