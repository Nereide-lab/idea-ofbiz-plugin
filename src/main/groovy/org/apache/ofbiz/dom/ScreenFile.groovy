package org.apache.ofbiz.dom

import com.intellij.util.xml.*
import com.intellij.util.xmlb.annotations.Attribute

interface ScreenFile extends DomElement {

    @SubTagList("screen")
    List<Screen> getScreens()


    interface Screen extends DomElement {
        @NameValue
        @Stubbed
        @Attribute("name")
        GenericAttributeValue<String> getName()
    }
}