package fr.nereide.dom.file

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.SubTagList
import fr.nereide.dom.element.serviceeca.Eca

/**
 * Part of the OFBiz DOM description
 */
interface ServiceEcaFile extends DomElement {

    @SubTagList('eca')
    List<Eca> getEcas()

}
