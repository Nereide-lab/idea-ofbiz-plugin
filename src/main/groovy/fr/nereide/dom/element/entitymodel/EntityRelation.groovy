package fr.nereide.dom.element.entitymodel

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue
import com.intellij.util.xml.SubTagList
import com.intellij.util.xmlb.annotations.Attribute

interface EntityRelation extends DomElement {
    @NameValue
    @Attribute("fk-name")
    GenericAttributeValue<String> getFkName()

    @Attribute("type")
    GenericAttributeValue<String> getType()

    @Attribute("rel-entity-name")
    GenericAttributeValue<String> getRelEntityName()

    @Attribute("title")
    GenericAttributeValue<String> getTitle()

    @SubTagList("key-map")
    List<RelationKeyMap> getKeyMaps()
}
