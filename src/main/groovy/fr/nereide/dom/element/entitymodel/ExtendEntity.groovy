package fr.nereide.dom.element.entitymodel

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue
import com.intellij.util.xml.SubTagList
import com.intellij.util.xmlb.annotations.Attribute

/**
 * Part of the OFBiz DOM description
 */
interface ExtendEntity extends DomElement {

    @NameValue
    @Attribute('entity-name')
    GenericAttributeValue<String> getEntityName()

    @SubTagList('field')
    List<EntityField> getFields()

}
