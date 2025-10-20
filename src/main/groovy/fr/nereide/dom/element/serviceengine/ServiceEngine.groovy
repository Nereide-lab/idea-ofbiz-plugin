package fr.nereide.dom.element.serviceengine

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.SubTagList

/**
 * Part of the OFBiz DOM description
 */
interface ServiceEngine extends DomElement {

    @SubTagList('engine')
    List<Engine> getEngines()

}
