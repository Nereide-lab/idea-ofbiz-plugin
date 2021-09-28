package org.apache.ofbiz.dom

import com.intellij.util.xml.*
import com.intellij.util.xmlb.annotations.Attribute

interface MenuFile extends DomElement {

    @SubTagList("menu")
    List<Menu> getMenus()

    interface Menu extends DomElement {
        @NameValue
        @Stubbed
        @Attribute("name")
        GenericAttributeValue<String> getName()
    }
}