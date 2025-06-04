package fr.nereide.dom.element.entityeca

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.SubTagList
import com.intellij.util.xmlb.annotations.Attribute
import fr.nereide.dom.element.commoneca.Action
import fr.nereide.dom.element.commoneca.Condition

interface Eca extends DomElement {

    @Attribute('entity')
    GenericAttributeValue<String> getEntity()

    @Attribute('operation')
    GenericAttributeValue<String> getOperation()

    @Attribute('event')
    GenericAttributeValue<String> getEvent()

    @SubTagList('condition')
    List<Condition> getConditions()

    @SubTagList('action')
    List<Action> getActions()
}