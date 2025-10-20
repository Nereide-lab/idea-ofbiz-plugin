package fr.nereide.dom.element.serviceengine

import com.intellij.util.xml.Attribute
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue
import com.intellij.util.xml.Stubbed

/**
 * Part of the OFBiz DOM description
 */
interface Engine extends DomElement {

    @NameValue
    @Stubbed
    @Attribute('name')
    GenericAttributeValue<String> getName()

}
