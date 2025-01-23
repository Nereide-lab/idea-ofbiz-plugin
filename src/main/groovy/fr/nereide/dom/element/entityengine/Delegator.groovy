package fr.nereide.dom.element.entityengine

import com.intellij.util.xml.Attribute
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue
import com.intellij.util.xml.SubTagList

interface Delegator extends DomElement {
    @NameValue
    @Attribute('name')
    GenericAttributeValue<String> getName()

    @SubTagList('group-map')
    List<DelegatorGroupMap> getGroupMaps()

}
