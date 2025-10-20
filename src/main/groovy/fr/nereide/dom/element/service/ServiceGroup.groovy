package fr.nereide.dom.element.service

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.SubTagList

/**
 * Part of the OFBiz DOM description
 */
interface ServiceGroup extends DomElement {

    @SubTagList('invoke')
    List<ServiceGroupInvoke> getInvokes()

}
