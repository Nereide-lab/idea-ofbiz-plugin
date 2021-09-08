package org.apache.ofbiz.dom

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomFileDescription

class EntityModelFileDescription<S extends DomElement> extends DomFileDescription {
    private static final String rootTagName = "entitymodel"

    EntityModelFileDescription() {
        super(EntityModelFile.class, rootTagName)
    }

}
