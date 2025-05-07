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

package fr.nereide.reference.common

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.util.xml.DomElement
import fr.nereide.dom.element.entitymodel.EntityRelation
import fr.nereide.project.OfbizProjectHelper
import org.jetbrains.annotations.Nullable

class EntityReference extends PsiReferenceBase<PsiElement> {
    EntityReference(PsiElement entityName) {
        super(entityName)
    }

    @Nullable
    PsiElement resolve() {
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(this.getElement().project)
        DomElement definition = ph.getEntity(this.getValue())
        if (!definition) definition = ph.getViewEntity(this.getValue())
        if (!definition) definition = ph.getEntity(getEntityNameFromGetRelated(ph))
        return definition != null ? definition.getXmlElement() : null
    }

    String getEntityNameFromGetRelated(OfbizProjectHelper ph) {
        String wantedString = this.getValue()
        if (!wantedString) return null
        List<EntityRelation> relations = ph.getAllEntityRelations()
        EntityRelation matchingRel = relations.find { EntityRelation rel ->
            wantedString == rel.getTitle().getValue() + rel.getRelEntityName().getValue()
        }
        if (!matchingRel) return null
        return matchingRel.getRelEntityName().getValue()
    }
}
