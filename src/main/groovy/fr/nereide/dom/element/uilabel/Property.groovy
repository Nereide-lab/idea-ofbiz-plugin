package fr.nereide.dom.element.uilabel

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue
import com.intellij.util.xml.SubTagList
import com.intellij.util.xmlb.annotations.Attribute

/**
 * Part of the OFBiz DOM description
 */
interface Property extends DomElement {

    @NameValue
    @Attribute('key')
    GenericAttributeValue<String> getKey()

    @SubTagList('value')
    List<PropertyValue> getPropertyValues()

}
