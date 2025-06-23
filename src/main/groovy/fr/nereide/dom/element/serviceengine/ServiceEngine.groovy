package fr.nereide.dom.element.serviceengine

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.SubTagList

interface ServiceEngine extends DomElement {

    @SubTagList("engine")
    List<Engine> getEngines()
}
