package fr.nereide.dom.element.entitymodel

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue
import com.intellij.util.xmlb.annotations.Attribute

import javax.xml.bind.annotation.XmlValue

/**
 * Part of the OFBiz DOM description
 */
interface EntityField extends DomElement {

    @NameValue
    @XmlValue
    @Attribute('name')
    GenericAttributeValue<String> getName()

    @Attribute('type')
    GenericAttributeValue<String> getType()

}
