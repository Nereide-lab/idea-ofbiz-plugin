package fr.nereide.dom.element.controller

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue
import com.intellij.util.xmlb.annotations.Attribute

/**
 * Part of the OFBiz DOM description
 */
interface RequestMap extends DomElement {

    @NameValue
    @Attribute('uri')
    GenericAttributeValue<String> getUri()

    @Attribute('method')
    GenericAttributeValue<String> getMethod()

}
