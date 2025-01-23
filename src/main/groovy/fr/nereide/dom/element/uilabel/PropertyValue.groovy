package fr.nereide.dom.element.uilabel

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.TagValue
import com.intellij.util.xmlb.annotations.Attribute

interface PropertyValue extends DomElement {
    @Attribute('lang')
    XmlOfbizAttrValue<String> getLang()

    @TagValue
    String getTagValue()
}
