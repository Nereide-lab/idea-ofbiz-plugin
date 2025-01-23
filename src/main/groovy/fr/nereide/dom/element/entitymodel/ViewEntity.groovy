package fr.nereide.dom.element.entitymodel

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.GenericDomValue
import com.intellij.util.xml.NameValue
import com.intellij.util.xml.Stubbed
import com.intellij.util.xml.SubTag
import com.intellij.util.xml.SubTagList
import com.intellij.util.xmlb.annotations.Attribute

import javax.xml.bind.annotation.XmlValue

interface ViewEntity extends DomElement {
    @Attribute("title")
    GenericAttributeValue<String> getTitle()

    @NameValue
    @XmlValue
    @Stubbed
    @Attribute("entity-name")
    GenericAttributeValue<String> getEntityName()

    @SubTag("description")
    GenericDomValue<String> getDescription()

    @Attribute("package-name")
    GenericAttributeValue<String> getPackageName()

    @SubTagList("member-entity")
    List<ViewEntityMember> getMemberEntities()

    @SubTagList("alias-all")
    List<AliasAll> getAliasAlls()

    @SubTagList("alias")
    List<Alias> getAliases()

    @SubTagList("view-link")
    List<EntityRelation> getViewLinks()
}
