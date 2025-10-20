package fr.nereide.dom.element.screen

import com.intellij.util.xml.Attribute
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue

/**
 * Part of the OFBiz DOM description
 */
interface IncludeScreen extends DomElement {

    @Attribute('name')
    GenericAttributeValue<String> getName()

    @Attribute('location')
    GenericAttributeValue<String> getLocation()

}
