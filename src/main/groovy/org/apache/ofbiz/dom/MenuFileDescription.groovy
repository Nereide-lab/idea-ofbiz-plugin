package org.apache.ofbiz.dom

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileDescription

class MenuFileDescription<S extends DomElement> extends DomFileDescription {
    private static final String rootTagName = "menus"

    MenuFileDescription() {
        super(MenuFile.class, rootTagName)
    }
}
