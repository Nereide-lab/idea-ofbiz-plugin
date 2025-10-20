package fr.nereide.dom.element.service

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.GenericDomValue
import com.intellij.util.xml.NameValue
import com.intellij.util.xml.Stubbed
import com.intellij.util.xml.SubTag
import com.intellij.util.xml.SubTagList
import com.intellij.util.xmlb.annotations.Attribute

/**
 * Part of the OFBiz DOM description
 */
interface Service extends DomElement {

    @NameValue
    @Stubbed
    @Attribute('name')
    GenericAttributeValue<String> getName()

    @Attribute('engine')
    GenericAttributeValue<String> getEngine()

    @Attribute('location')
    GenericAttributeValue<String> getLocation()

    @Attribute('invoke')
    GenericAttributeValue<String> getInvoke()

    @Attribute('auth')
    GenericAttributeValue<String> getAuth()

    @Attribute('default-entity-name')
    GenericAttributeValue<String> getDefaultEntityName()

    @SubTagList('implements')
    List<ServiceImplements> getImplements()

    @SubTag('description')
    GenericDomValue<String> getDescription()

    @SubTagList('attribute')
    List<ServiceAttribute> getAttributes()

    @SubTagList('auto-attributes')
    List<ServiceAutoAttribute> getAutoAttributes()

    @SubTag('group')
    ServiceGroup getGroup()

}
