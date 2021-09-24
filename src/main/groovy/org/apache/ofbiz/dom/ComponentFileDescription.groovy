package org.apache.ofbiz.dom

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileDescription

class ComponentFileDescription<S extends DomElement> extends DomFileDescription<ComponentFile> {
    private static final String rootTagName = "ofbiz-component"

    ComponentFileDescription() { super(ComponentFile.class, rootTagName) }
}
