package org.apache.ofbiz.dom

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileDescription

class ScreenFileDescription<S extends DomElement> extends DomFileDescription {
    private static final String rootTagName = "screens"

    ScreenFileDescription() {
        super(ScreenFile.class, rootTagName)
    }
}
