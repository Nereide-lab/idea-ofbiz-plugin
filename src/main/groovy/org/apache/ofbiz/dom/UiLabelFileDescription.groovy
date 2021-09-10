package org.apache.ofbiz.dom

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileDescription

class UiLabelFileDescription<S extends DomElement> extends DomFileDescription {
    private static final String rootTagName = "resource"

    UiLabelFileDescription() {
        super(UiLabelFile.class, rootTagName)
    }
}
