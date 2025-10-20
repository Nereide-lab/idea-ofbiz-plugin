package fr.nereide.dom.element.uilabel

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.TagValue
import com.intellij.util.xmlb.annotations.Attribute

/**
 * Part of the OFBiz DOM description
 */
interface PropertyValue extends DomElement {

    @Attribute('lang')
    XmlOfbizAttrValue<String> getLang()

    @TagValue
    String getTagValue()

}
