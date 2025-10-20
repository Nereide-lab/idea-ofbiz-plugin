package fr.nereide.dom.element.entityengine

import com.intellij.util.xml.Attribute
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue
import com.intellij.util.xml.SubTagList

/**
 * Part of the OFBiz DOM description
 */
interface Delegator extends DomElement {

    @NameValue
    @Attribute('name')
    GenericAttributeValue<String> getName()

    @SubTagList('group-map')
    List<DelegatorGroupMap> getGroupMaps()

}
