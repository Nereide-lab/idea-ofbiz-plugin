package org.apache.ofbiz.dom

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileDescription

class FormFileDescription<S extends DomElement> extends DomFileDescription {
    private static final String rootTagName = "forms"

    FormFileDescription() {
        super(FormFile.class, rootTagName)
    }
}
