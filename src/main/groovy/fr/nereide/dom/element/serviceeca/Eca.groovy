package fr.nereide.dom.element.serviceeca

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.SubTagList
import com.intellij.util.xmlb.annotations.Attribute
import fr.nereide.dom.element.commoneca.Action
import fr.nereide.dom.element.commoneca.Condition

/**
 * Part of the OFBiz DOM description
 */
interface Eca extends DomElement {

    @Attribute('service')
    GenericAttributeValue<String> getService()

    @Attribute('event')
    GenericAttributeValue<String> getEvent()

    @SubTagList('condition')
    List<Condition> getConditions()

    @SubTagList('action')
    List<Action> getActions()

}
