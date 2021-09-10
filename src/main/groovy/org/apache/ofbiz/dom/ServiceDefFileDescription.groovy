package org.apache.ofbiz.dom

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileDescription

class ServiceDefFileDescription<S extends DomElement> extends DomFileDescription {
    private static final String rootTagName = "services"

    ServiceDefFileDescription() {
        super(ServiceDefFile.class, rootTagName)
    }
}