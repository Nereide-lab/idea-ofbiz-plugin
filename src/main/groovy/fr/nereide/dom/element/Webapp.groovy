package fr.nereide.dom.element

import com.intellij.util.xml.Attribute
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue

interface Webapp extends DomElement {
    @NameValue
    @Attribute("name")
    GenericAttributeValue<String> getName()

    @Attribute("title")
    GenericAttributeValue<String> getTitle()

    @Attribute("server")
    GenericAttributeValue<String> getServer()

    @Attribute("location")
    GenericAttributeValue<String> getLocation()

    @Attribute("base-permission")
    GenericAttributeValue<String> getBasePermission()

    @Attribute("app-shortcut-screen")
    GenericAttributeValue<String> getAppShortcutScreen()

    @Attribute("mount-point")
    GenericAttributeValue<String> getMountPoint()
}
