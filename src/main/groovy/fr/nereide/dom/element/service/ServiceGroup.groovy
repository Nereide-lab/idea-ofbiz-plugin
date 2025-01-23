package fr.nereide.dom.element.service

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.SubTagList

interface ServiceGroup extends DomElement {

    @SubTagList("invoke")
    List<ServiceGroupInvoke> getInvokes()
}
