package org.apache.ofbiz.dom

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.GenericDomValue
import com.intellij.util.xml.NameValue
import com.intellij.util.xml.Stubbed
import com.intellij.util.xml.SubTag
import com.intellij.util.xml.SubTagList
import com.intellij.util.xmlb.annotations.Attribute

import javax.xml.bind.annotation.XmlValue

interface EntityModelFile extends DomElement {
    @SubTagList("entity")
    List<Entity> getEntities()

    @SubTagList("view-entity")
    List<ViewEntity> getViewEntities()

//    @SubTagList("extend-entity")
//    List<ExtendEntity> getExtendEntities()

    interface Entity extends DomElement {
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

        @SubTagList("field")
        List<DomElement> getFields()

        @SubTagList("prim-key")
        List<DomElement> getPrimKeys()

        @SubTagList("relation")
        List<RelationsTag> getRelations()
    }

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
        List<DomElement> getMemberEntities()

        @SubTagList("alias")
        List<DomElement> getAliases()

        @SubTagList("view-link")
        List<RelationsTag> getViewLinks()
    }

    interface RelationsTag extends DomElement {
        @NameValue
        @Attribute("fk-name")
        GenericAttributeValue<String> getFkName()

        @NameValue
        @Attribute("rel-entity-name")
        GenericAttributeValue<String> getRelEntityName()

        @SubTagList("key-map")
        List<DomElement> getKeyMaps()
    }
}