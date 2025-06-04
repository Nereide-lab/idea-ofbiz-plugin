package fr.nereide.dom.file

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.SubTagList
import fr.nereide.dom.element.entityeca.Eca

interface EntityEcaFile extends DomElement {

    @SubTagList('eca')
    List<Eca> getEcas()
}
