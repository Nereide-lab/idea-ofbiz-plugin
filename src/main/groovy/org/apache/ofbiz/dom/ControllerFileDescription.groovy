package org.apache.ofbiz.dom

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileDescription

class ControllerFileDescription<S extends DomElement> extends DomFileDescription {
    private static final String rootTagName = "site-conf"

    ControllerFileDescription() { super(ControllerFile.class, rootTagName) }
}
