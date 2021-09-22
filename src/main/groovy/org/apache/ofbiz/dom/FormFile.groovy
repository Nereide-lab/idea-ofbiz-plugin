package org.apache.ofbiz.dom

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue
import com.intellij.util.xml.Stubbed
import com.intellij.util.xml.SubTagList
import com.intellij.util.xmlb.annotations.Attribute

interface FormFile extends DomElement {

    @SubTagList("form")
    List<Form> getForms()

    @SubTagList("grid")
    List<Grid> getGrids()

    interface Form extends DomElement {
        @NameValue
        @Stubbed
        @Attribute("name")
        GenericAttributeValue<String> getName()

        @Attribute("type")
        GenericAttributeValue<String> getType()

        @Attribute("target")
        GenericAttributeValue<String> getTarget()

        @SubTagList("field")
        List<FormField> getFormFields()
    }

    interface Grid extends DomElement {
        @NameValue
        @Stubbed
        @Attribute("name")
        GenericAttributeValue<String> getName()

        @Attribute("type")
        GenericAttributeValue<String> getType()

        @Attribute("target")
        GenericAttributeValue<String> getTarget()

        @SubTagList("field")
        List<FormField> getFormFields()
    }

    interface FormField extends DomElement {
        @NameValue
        @Stubbed
        @Attribute("name")
        GenericAttributeValue<String> getName()
    }
}