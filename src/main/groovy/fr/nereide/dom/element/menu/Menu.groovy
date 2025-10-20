package fr.nereide.dom.element.menu

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue
import com.intellij.util.xml.Stubbed
import com.intellij.util.xmlb.annotations.Attribute

/**
 * Part of the OFBiz DOM description
 */
interface Menu extends DomElement {

    @NameValue
    @Stubbed
    @Attribute('name')
    GenericAttributeValue<String> getName()

}
