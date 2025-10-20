package fr.nereide.dom.element.entityengine

import com.intellij.util.xml.Attribute
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue

/**
 * Part of the OFBiz DOM description
 */
interface DelegatorGroupMap extends DomElement {

    @Attribute('group-name')
    GenericAttributeValue<String> getGroupName()

    @Attribute('datasource-name')
    GenericAttributeValue<String> getDatasourceName()

}
