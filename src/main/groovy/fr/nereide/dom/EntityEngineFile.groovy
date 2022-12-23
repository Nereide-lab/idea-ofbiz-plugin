package fr.nereide.dom

import com.intellij.util.xml.Attribute
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue
import com.intellij.util.xml.SubTagList

interface EntityEngineFile extends DomElement {

    @SubTagList('delegator')
    List<Delegator> getDelegators()

    @SubTagList('datasource')
    List<Datasource> getDatasources()

    interface Delegator extends DomElement {
        @NameValue
        @Attribute('name')
        GenericAttributeValue<String> getName()

        @SubTagList('group-map')
        List<DelegatorGroupMap> getGroupMaps()

    }

    interface DelegatorGroupMap extends DomElement {
        @Attribute('group-name')
        GenericAttributeValue<String> getGroupName()

        @Attribute('datasource-name')
        GenericAttributeValue<String> getDatasourceName()
    }

    interface Datasource extends DomElement {
        @NameValue
        @Attribute('name')
        GenericAttributeValue<String> getName()
    }
}
