package fr.nereide.dom.element.commoneca

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xmlb.annotations.Attribute

interface Condition extends DomElement {

    @Attribute('field-name')
    GenericAttributeValue<String> getFieldName()

    @Attribute('operator')
    GenericAttributeValue<String> getOperator()

    @Attribute('value')
    GenericAttributeValue<String> getValue()
}