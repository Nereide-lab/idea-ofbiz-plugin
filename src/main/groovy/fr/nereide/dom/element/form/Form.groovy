package fr.nereide.dom.element.form

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue
import com.intellij.util.xml.Stubbed
import com.intellij.util.xml.SubTagList
import com.intellij.util.xmlb.annotations.Attribute

/**
 * Part of the OFBiz DOM description
 */
interface Form extends DomElement {

    @NameValue
    @Stubbed
    @Attribute('name')
    GenericAttributeValue<String> getName()

    @Attribute('type')
    GenericAttributeValue<String> getType()

    @Attribute('target')
    GenericAttributeValue<String> getTarget()

    @SubTagList('field')
    List<FormField> getFormFields()

}
