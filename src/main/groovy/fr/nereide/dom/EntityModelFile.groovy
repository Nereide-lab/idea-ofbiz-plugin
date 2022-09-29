/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License") you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package fr.nereide.dom

import com.intellij.util.xml.*
import com.intellij.util.xmlb.annotations.Attribute

import javax.xml.bind.annotation.XmlValue

interface EntityModelFile extends DomElement {
    @SubTagList("entity")
    List<Entity> getEntities()

    @SubTagList("view-entity")
    List<ViewEntity> getViewEntities()

    @SubTagList("extend-entity")
    List<ExtendEntity> getExtendEntities()

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
        List<EntityField> getFields()

        @SubTagList("prim-key")
        List<EntityPrimKey> getPrimKeys()

        @SubTagList("relation")
        List<RelationTag> getRelations()
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
        List<ViewEntityMember> getMemberEntities()

        @SubTagList("alias-all")
        List<AliasAll> getAliasAllList()

        @SubTagList("alias")
        List<Alias> getAliases()

        @SubTagList("view-link")
        List<RelationTag> getViewLinks()
    }

    interface RelationTag extends DomElement {
        @NameValue
        @Attribute("fk-name")
        GenericAttributeValue<String> getFkName()

        @Attribute("type")
        GenericAttributeValue<String> getType()

        @NameValue
        @Attribute("rel-entity-name")
        GenericAttributeValue<String> getRelEntityName()

        @SubTagList("key-map")
        List<RelationKeyMap> getKeyMaps()
    }

    interface RelationKeyMap extends DomElement {
        @Attribute("field-name")
        GenericAttributeValue<String> getFieldName()

        @Attribute("rel-field-name")
        GenericAttributeValue<String> getRelFieldName()
    }

    interface EntityField extends DomElement {
        @NameValue
        @XmlValue
        @Attribute("name")
        GenericAttributeValue<String> getName()

        @Attribute("type")
        GenericAttributeValue<String> getType()
    }

    interface ViewEntityMember extends DomElement {
        @Attribute("entity-alias")
        GenericAttributeValue<String> getEntityAlias()

        @Attribute("entity-name")
        GenericAttributeValue<String> getEntityName()
    }

    interface AliasAll extends DomElement {
        @Attribute("entity-alias")
        GenericAttributeValue<String> getEntityAlias()

        @Attribute("prefix")
        GenericAttributeValue<String> getPrefix()

        @SubTagList("exclude")
        List<AliasAllExclude> getAliasAllExcludes()
    }

    interface AliasAllExclude extends DomElement {
        @Attribute("field")
        GenericAttributeValue<String> getField()
    }

    interface Alias extends DomElement {
        @Attribute("name")
        GenericAttributeValue<String> getName()

        @Attribute("entity-alias")
        GenericAttributeValue<String> getEntityAlias()

        @Attribute("field")
        GenericAttributeValue<String> getField()
    }

    interface EntityPrimKey extends DomElement {
        @Attribute('field')
        GenericAttributeValue<String> getField()
    }

    interface ExtendEntity extends DomElement {
        @NameValue
        @Attribute("entity-name")
        GenericAttributeValue<String> getEntityName()

        @SubTagList("field")
        List<EntityField> getFields()
    }
}
