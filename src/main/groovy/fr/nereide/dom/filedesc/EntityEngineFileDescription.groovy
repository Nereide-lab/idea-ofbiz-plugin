package fr.nereide.dom.filedesc

import com.intellij.util.xml.DomFileDescription
import com.intellij.util.xml.DomFileElement
import fr.nereide.dom.file.EntityEngineFile

/**
 * Part of the OFBiz DOM description
 */
class EntityEngineFileDescription<S extends DomFileElement> extends DomFileDescription {

    private static final String ROOT_TAG = 'entity-config'

    EntityEngineFileDescription() {
        super(EntityEngineFile, ROOT_TAG)
    }

}
