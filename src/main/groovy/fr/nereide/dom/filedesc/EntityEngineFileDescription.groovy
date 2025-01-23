package fr.nereide.dom.filedesc

import com.intellij.util.xml.DomFileDescription
import com.intellij.util.xml.DomFileElement
import fr.nereide.dom.file.EntityEngineFile

class EntityEngineFileDescription<S extends DomFileElement> extends DomFileDescription {

    private static final String rootTagName = "entity-config"

    EntityEngineFileDescription() {
        super(EntityEngineFile, rootTagName)
    }
}
