package fr.nereide.dom.element.controller

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue
import com.intellij.util.xmlb.annotations.Attribute

/**
 * Part of the OFBiz DOM description
 */
interface ViewMap extends DomElement {

    @NameValue
    @Attribute('name')
    GenericAttributeValue<String> getName()

    @Attribute('type')
    GenericAttributeValue<String> getType()

}
