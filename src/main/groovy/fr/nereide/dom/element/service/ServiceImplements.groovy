package fr.nereide.dom.element.service

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue
import com.intellij.util.xml.Stubbed
import com.intellij.util.xmlb.annotations.Attribute

/**
 * Part of the OFBiz DOM description
 */
interface ServiceImplements extends DomElement {

    @NameValue
    @Stubbed
    @Attribute('service')
    GenericAttributeValue<String> getService()

    @Attribute('optional')
    GenericAttributeValue<String> getOptional()

}
