package fr.nereide.dom.file

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.SubTagList
import fr.nereide.dom.element.entityengine.Datasource
import fr.nereide.dom.element.entityengine.Delegator

/**
 * Part of the OFBiz DOM description
 */
interface EntityEngineFile extends DomElement {

    @SubTagList('delegator')
    List<Delegator> getDelegators()

    @SubTagList('datasource')
    List<Datasource> getDatasources()

}
