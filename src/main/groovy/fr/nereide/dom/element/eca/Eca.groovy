package fr.nereide.dom.element.eca

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.SubTagList
import com.intellij.util.xmlb.annotations.Attribute

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