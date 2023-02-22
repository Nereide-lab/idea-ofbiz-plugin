/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package fr.nereide.project


import com.intellij.patterns.PsiElementPattern
import com.intellij.patterns.XmlAttributeValuePattern
import fr.nereide.project.pattern.FieldTypeCondition

import static com.intellij.patterns.PlatformPatterns.psiElement
import static com.intellij.patterns.PsiJavaPatterns.literalExpression
import static com.intellij.patterns.PsiJavaPatterns.psiMethod
import static com.intellij.patterns.StandardPatterns.string
import static com.intellij.patterns.XmlPatterns.xmlAttribute
import static com.intellij.patterns.XmlPatterns.xmlAttributeValue
import static com.intellij.patterns.XmlPatterns.xmlTag
import static fr.nereide.dom.CompoundFileDescription.*
import static org.jetbrains.plugins.groovy.lang.psi.patterns.GroovyPatterns.groovyLiteralExpression
import static org.jetbrains.plugins.groovy.lang.psi.patterns.GroovyPatterns.namedArgument

class OfbizPatterns {

    // =============================================================
    //                      JAVA
    // =============================================================
    class JAVA {
        public static final PsiElementPattern SERVICE_CALL = psiElement().andOr(
                literalExpression()
                        .methodCallParameter(0, psiMethod().withName("makeValidContext")
                                .definedInClass("org.apache.ofbiz.service.DispatchContext")),

                literalExpression()
                        .methodCallParameter(0, psiMethod().withName("runSync")
                                .definedInClass("org.apache.ofbiz.service.LocalDispatcher")),

                literalExpression()
                        .methodCallParameter(0, psiMethod().withName("runAsync")
                                .definedInClass("org.apache.ofbiz.service.LocalDispatcher")),

                literalExpression()
                        .methodCallParameter(0, psiMethod().withName("runAsyncWait")
                                .definedInClass("org.apache.ofbiz.service.LocalDispatcher")),

                literalExpression()
                        .methodCallParameter(0, psiMethod().withName("runSyncIgnore")
                                .definedInClass("org.apache.ofbiz.service.LocalDispatcher")),

                literalExpression()
                        .methodCallParameter(0, psiMethod().withName("runSyncNewTrans")
                                .definedInClass("org.apache.ofbiz.service.LocalDispatcher")),

                literalExpression()
                        .methodCallParameter(0, psiMethod().withName("runService")
                                .definedInClass("org.apache.ofbiz.base.util.ScriptHelper")),

                literalExpression()
                        .methodCallParameter(0, psiMethod().withName("schedule")
                                .definedInClass("org.apache.ofbiz.service.LocalDispatcher")),
        )

        public static final PsiElementPattern ENTITY_CALL = psiElement().andOr(
                literalExpression().methodCallParameter(0, psiMethod()
                        .withName("find").definedInClass("org.apache.ofbiz.entity.Delegator")),

                literalExpression().methodCallParameter(0, psiMethod()
                        .withName("findOne").definedInClass("org.apache.ofbiz.entity.Delegator")),

                literalExpression().methodCallParameter(0, psiMethod()
                        .withName("findAll").definedInClass("org.apache.ofbiz.entity.Delegator")),

                literalExpression().methodCallParameter(0, psiMethod()
                        .withName("findCountByCondition").definedInClass("org.apache.ofbiz.entity.Delegator")),

                literalExpression().methodCallParameter(0, psiMethod()
                        .withName("findList").definedInClass("org.apache.ofbiz.entity.Delegator")),

                literalExpression().methodCallParameter(1, psiMethod()
                        .withName("addMemberEntity").definedInClass("org.apache.ofbiz.entity.model.DynamicViewEntity")),

                literalExpression().methodCallParameter(1, psiMethod().
                        withName("makeGenericValue").definedInClass("org.apache.ofbiz.entityext.data.EntityDataServices")),

                literalExpression().methodCallParameter(0, psiMethod()
                        .withName("from").definedInClass("org.apache.ofbiz.entity.util.EntityQuery")),

                literalExpression().methodCallParameter(0,
                        psiMethod()
                                .withName("makeValue")
                                .definedInClass("org.apache.ofbiz.entity.Delegator"))
        )

        public static final PsiElementPattern LABEL_CALL = psiElement().andOr(
                literalExpression().methodCallParameter(1, psiMethod()
                        .withName("getMessage").definedInClass("org.apache.ofbiz.base.util.UtilProperties"))
        )

        public static final PsiElementPattern SERVICE_CALL_COMPL = psiElement()
                .inside(SERVICE_CALL)
        public static final PsiElementPattern ENTITY_CALL_COMPL = psiElement()
                .inside(ENTITY_CALL)
    }

    // =============================================================
    //                      GROOVY
    // =============================================================
    class GROOVY {
        public static final PsiElementPattern SERVICE_CALL = psiElement().andOr(
                groovyLiteralExpression().methodCallParameter(0, psiMethod().withName("runSync")
                        .definedInClass("org.apache.ofbiz.service.LocalDispatcher")),

                groovyLiteralExpression().methodCallParameter(0, psiMethod().withName("runAsync")
                        .definedInClass("org.apache.ofbiz.service.LocalDispatcher")),

                groovyLiteralExpression().methodCallParameter(0, psiMethod().withName("runAsyncWait")
                        .definedInClass("org.apache.ofbiz.service.LocalDispatcher")),

                groovyLiteralExpression().methodCallParameter(0, psiMethod().withName("runSyncIgnore")
                        .definedInClass("org.apache.ofbiz.service.LocalDispatcher")),

                groovyLiteralExpression().methodCallParameter(0, psiMethod().withName("runSyncNewTrans")
                        .definedInClass("org.apache.ofbiz.service.LocalDispatcher")),

                groovyLiteralExpression().methodCallParameter(0, psiMethod().withName("runService")),

                //TODO : bétonner un peu pour éviter les soucis de référence non trouvée ?
                groovyLiteralExpression().withParent(namedArgument().withLabel('service')
                )
        )

        public static final PsiElementPattern ENTITY_CALL = psiElement().andOr(
                groovyLiteralExpression().methodCallParameter(0, psiMethod().withName("find")
                        .definedInClass("org.apache.ofbiz.entity.Delegator")),

                groovyLiteralExpression().methodCallParameter(0, psiMethod().withName("findOne")
                        .definedInClass("org.apache.ofbiz.entity.Delegator")),

                groovyLiteralExpression().methodCallParameter(0, psiMethod().withName("findAll")
                        .definedInClass("org.apache.ofbiz.entity.Delegator")),

                // TODO : Marche pas
                groovyLiteralExpression().methodCallParameter(0, psiMethod().withName("findCountByCondition")
                        .definedInClass("org.apache.ofbiz.entity.Delegator")),

                // TODO : Marche pas
                groovyLiteralExpression().methodCallParameter(0, psiMethod().withName("findList")
                        .definedInClass("org.apache.ofbiz.entity.Delegator")),

                groovyLiteralExpression().methodCallParameter(1, psiMethod().withName("addMemberEntity")
                        .definedInClass("org.apache.ofbiz.entity.model.DynamicViewEntity")),

                groovyLiteralExpression().methodCallParameter(1, psiMethod().withName("makeGenericValue")
                        .definedInClass("org.apache.ofbiz.entityext.data.EntityDataServices")),

                groovyLiteralExpression().methodCallParameter(0, psiMethod().withName("from")),

                groovyLiteralExpression().methodCallParameter(0, psiMethod().withName("makeValue")),

                groovyLiteralExpression().methodCallParameter(0, psiMethod().withName("findOne"))
        )

        public static final PsiElementPattern LABEL_CALL = psiElement().andOr(
                groovyLiteralExpression().methodCallParameter(1, psiMethod().withName("getMessage")
                        .definedInClass("org.apache.ofbiz.base.util.UtilProperties"))
        )

        public static final PsiElementPattern GENERIC_VALUE_ATTRIBUTE = psiElement().andOr(
                psiElement().afterLeafSkipping(
                        psiElement().withText("."),
                        psiElement().withParent(psiElement().with(new FieldTypeCondition(
                                "GenericValueTypePattern",
                                'org.apache.ofbiz.entity.GenericValue', 'GenericValue'))
                        )
                )
        )

        public static final PsiElementPattern SERVICE_CALL_COMPL = psiElement().inside(SERVICE_CALL)
        public static final PsiElementPattern ENTITY_CALL_COMPL = psiElement().inside(ENTITY_CALL)
        public static final PsiElementPattern GENERIC_VALUE_ATTRIBUTE_COMPL = psiElement().inside(GENERIC_VALUE_ATTRIBUTE)

        public static final PsiElementPattern GROOVY_LOOP_PATTERN = psiElement().andOr(
                psiElement().withText(string().contains('forEach')),
                psiElement().withText(string().contains('stream'))
        )
    }

    // =============================================================
    //                      XML
    // =============================================================
    class XML {
        public static final XmlAttributeValuePattern URI_CALL = xmlAttributeValue().andOr(
                xmlAttributeValue()
                        .inside(xmlAttribute()
                                .withName("target")
                                .inside(xmlTag().withName("form"))
                        ),
                xmlAttributeValue()
                        .inside(xmlAttribute()
                                .withName("target")
                                .inside(xmlTag()
                                        .withName("${FORM_NS_PREFIX}form")
                                        .withNamespace(FORM_NS_URL)
                                )
                        )
        )

        public static final XmlAttributeValuePattern RESPONSE_CALL = xmlAttributeValue().andOr(
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("value")
                                .inside(xmlTag()
                                        .withName("${SITE_CONF_NS_PREFIX}response")
                                        .withNamespace(SITE_CONF_NS_URL))),
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("value")
                                .inside(xmlTag()
                                        .withName("response"))
                )
        )

        public static final XmlAttributeValuePattern ENTITY_CALL = xmlAttributeValue()
                .inside(xmlAttribute()
                        .withName("entity", "entity-name", "default-entity-name", "rel-entity-name"))

        public static final XmlAttributeValuePattern SERVICE_DEF_CALL = xmlAttributeValue().andOr(
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("service", "service-name")),
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("invoke")
                                .inside(xmlTag()
                                        .withName("event")
                                        .withChild(xmlAttribute()
                                                .withName("type")
                                                .withValue(string().contains("service"))))
                ),
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("invoke")
                                .inside(xmlTag()
                                        .withName("${SITE_CONF_NS_PREFIX}event")
                                        .withNamespace(SITE_CONF_NS_URL)
                                        .withChild(xmlAttribute()
                                                .withName("type")
                                                .withValue(string().contains("service")))
                                )
                ),
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName('name')
                                .inside(xmlTag()
                                        .withName('invoke')
                                        .inside(xmlTag()
                                                .withName('group')
                                                .inside(xmlTag()
                                                        .withName('service')
                                                        .withAttributeValue('engine', 'group')
                                                )
                                        )
                                )
                )
        )

        public static final XmlAttributeValuePattern JAVA_EVENT_CALL = xmlAttributeValue().andOr(
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("invoke")
                                .inside(xmlTag()
                                        .withName("event")
                                        .withChild(xmlAttribute()
                                                .withName("type")
                                                .withValue(string().equalTo("java")))
                                )),
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("invoke")
                                .inside(xmlTag()
                                        .withName("${SITE_CONF_NS_PREFIX}event")
                                        .withNamespace(SITE_CONF_NS_URL)
                                        .withChild(xmlAttribute()
                                                .withName("type")
                                                .withValue(string().equalTo("java")))
                                )),
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("invoke")
                                .inside(xmlTag()
                                        .withName("service")
                                        .withChild(xmlAttribute()
                                                .withName("engine")
                                                .withValue(string().equalTo("java")))
                                ))
        )

        public static final XmlAttributeValuePattern LABEL_CALL = xmlAttributeValue().inside(
                xmlAttribute().withValue(string().startsWith('${uiLabelMap.'))
        )

        public static final XmlAttributeValuePattern FORM_CALL = xmlAttributeValue().andOr(
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .inside(xmlTag().withName("include-form"))
                                .withName("name"),
                ),
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .inside(xmlTag().withName("form"))
                                .withName("extends"),
                ),
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("name")
                                .inside(xmlTag()
                                        .withName("${FORM_NS_PREFIX}include-form")
                                        .withNamespace(FORM_NS_URL)
                                ),
                ),
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("name")
                                .inside(xmlTag()
                                        .withName("${SCREEN_NS_PREFIX}include-form")
                                        .withNamespace(SCREEN_NS_URL)
                                ),
                ),
        )

        public static final XmlAttributeValuePattern GRID_CALL = xmlAttributeValue().andOr(
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .inside(xmlTag().withName("include-grid"))
                                .withName("name"),
                ),
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .inside(xmlTag().withName("grid"))
                                .withName("extends"),
                ),
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("name")
                                .inside(xmlTag()
                                        .withName("${FORM_NS_PREFIX}include-grid")
                                        .withNamespace(FORM_NS_URL)
                                ),
                ),
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("name")
                                .inside(xmlTag()
                                        .withName("${SCREEN_NS_PREFIX}include-grid")
                                        .withNamespace(SCREEN_NS_URL)
                                ),
                )
        )

        public static final XmlAttributeValuePattern MENU_CALL = xmlAttributeValue().andOr(
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("navigation-menu-name")
                                .inside(xmlTag().withName("screenlet"))
                ),
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("name")
                                .inside(xmlTag().withName("include-menu"))
                ),
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("name")
                                .inside(xmlTag()
                                        .withName("${SCREEN_NS_PREFIX}include-menu")
                                        .withNamespace(SCREEN_NS_URL)
                                )
                ),
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("name")
                                .inside(xmlTag()
                                        .withName("${FORM_NS_PREFIX}include-menu")
                                        .withNamespace(FORM_NS_URL)
                                )
                )
        )

        public static final XmlAttributeValuePattern FILE_CALL = xmlAttributeValue()
                .inside(xmlAttribute()
                        .withName("entity-xml-url", "xml-resource", "extends-resource",
                                "resourceValue", "resource", "template", "page", "location", "image-location",
                                "component-location", "fallback-location", "default-fallback-location",
                                "default-location", "path")
                )

        public static final XmlAttributeValuePattern SCREEN_CALL = xmlAttributeValue().andOr(
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("name")
                                .inside(xmlTag().withName("include-screen"))
                ),
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("page")
                                .inside(xmlTag()
                                        .withName("view-map")
                                        .withChild(xmlAttribute()
                                                .withName("type")
                                                .withValue(string().equalTo("screen"))))
                ),
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("page")
                                .inside(xmlTag()
                                        .withName("${SITE_CONF_NS_PREFIX}view-map")
                                        .withNamespace(SITE_CONF_NS_URL)
                                        .withChild(xmlAttribute()
                                                .withName("type")
                                                .withValue(string().equalTo("screen"))))
                ),
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("name")
                                .inside(xmlTag()
                                        .withName("${SCREEN_NS_PREFIX}include-screen")
                                        .withNamespace(SCREEN_NS_URL)
                                )
                ),
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("name")
                                .inside(xmlTag()
                                        .withName("${FORM_NS_PREFIX}include-screen")
                                        .withNamespace(FORM_NS_URL)
                                )
                ),
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("name")
                                .withParent(xmlTag().withName("decorator-screen"))
                ),
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("name")
                                .inside(xmlTag()
                                        .withName("${SCREEN_NS_PREFIX}decorator-screen")
                                        .withNamespace(SCREEN_NS_URL)
                                )
                )
        )

        public static final XmlAttributeValuePattern GROOVY_SERVICE_METHOD = xmlAttributeValue().andOr(
                xmlAttributeValue().inside(
                        xmlAttribute()
                                .withName("invoke")
                                .inside(xmlTag()
                                        .withName("service")
                                        .withChild(
                                                xmlAttribute().withName("engine").withValue(string().contains("groovy"))
                                        )
                                )
                )
        )

        public static final XmlAttributeValuePattern DATASOURCE_CALL = xmlAttributeValue().andOr(
                xmlAttributeValue().inside(xmlAttribute().withName("datasource-name"))
        )

        public static final PsiElementPattern ENTITY_CALL_COMPL = psiElement()
                .inside(ENTITY_CALL)
        public static final PsiElementPattern SERVICE_DEF_CALL_COMPL = psiElement()
                .inside(SERVICE_DEF_CALL)
    }
}
