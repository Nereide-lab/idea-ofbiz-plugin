package fr.nereide.dom

import com.intellij.util.xml.DomFileDescription
import com.intellij.util.xml.DomFileElement

class EntityEngineFileDescription<S extends DomFileElement> extends DomFileDescription {

    private static final String rootTagName = "entity-config"

    EntityEngineFileDescription() {
        super(EntityEngineFile, rootTagName)
    }
}
