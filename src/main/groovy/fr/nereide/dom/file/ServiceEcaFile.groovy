package fr.nereide.dom.file

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.SubTagList
import fr.nereide.dom.element.eca.Eca

interface ServiceEcaFile extends DomElement {

    @SubTagList('eca')
    List<Eca> getEcas()
}
