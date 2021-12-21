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

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.patterns.PsiJavaPatterns
import com.intellij.patterns.XmlNamedElementPattern
import com.intellij.patterns.XmlPatterns
import org.jetbrains.plugins.groovy.lang.psi.patterns.GroovyPatterns

class OfbizPatterns {

    // =============================================================
    //                      JAVA
    // =============================================================
    public static final PsiElementPattern JAVA_SERVICE_CALL = PlatformPatterns.psiElement().andOr(
            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("makeValidContext")
                            .definedInClass("org.apache.ofbiz.service.DispatchContext"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runSync")
                            .definedInClass("org.apache.ofbiz.service.LocalDispatcher"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runAsync")
                            .definedInClass("org.apache.ofbiz.service.LocalDispatcher"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runAsyncWait")
                            .definedInClass("org.apache.ofbiz.service.LocalDispatcher"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runSyncIgnore")
                            .definedInClass("org.apache.ofbiz.service.LocalDispatcher"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runSyncNewTrans")
                            .definedInClass("org.apache.ofbiz.service.LocalDispatcher"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runService")
                            .definedInClass("org.apache.ofbiz.base.util.ScriptHelper"))),

            PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("makeValidContext")
                            .definedInClass("org.apache.ofbiz.service.DispatchContext")),

            PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runSync")
                            .definedInClass("org.apache.ofbiz.service.LocalDispatcher")),

            PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runAsync")
                            .definedInClass("org.apache.ofbiz.service.LocalDispatcher")),

            PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runAsyncWait")
                            .definedInClass("org.apache.ofbiz.service.LocalDispatcher")),

            PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runSyncIgnore")
                            .definedInClass("org.apache.ofbiz.service.LocalDispatcher")),

            PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runSyncNewTrans")
                            .definedInClass("org.apache.ofbiz.service.LocalDispatcher")),

            PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runService")
                            .definedInClass("org.apache.ofbiz.base.util.ScriptHelper"))
    )

    public static final PsiElementPattern JAVA_ENTITY_CALL = PlatformPatterns.psiElement().andOr(
            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("find")
                            .definedInClass("org.apache.ofbiz.entity.Delegator"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("findOne")
                            .definedInClass("org.apache.ofbiz.entity.Delegator"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("findAll")
                            .definedInClass("org.apache.ofbiz.entity.Delegator"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("findCountByCondition")
                            .definedInClass("org.apache.ofbiz.entity.Delegator"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("findList")
                            .definedInClass("org.apache.ofbiz.entity.Delegator"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(1, PsiJavaPatterns.psiMethod()
                            .withName("addMemberEntity")
                            .definedInClass("org.apache.ofbiz.entity.model.DynamicViewEntity"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(1, PsiJavaPatterns.psiMethod()
                            .withName("makeGenericValue")
                            .definedInClass("org.apache.ofbiz.entityext.data.EntityDataServices"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("from")
                            .definedInClass("org.apache.ofbiz.entity.util.EntityQuery"))),

            PsiJavaPatterns.literalExpression().methodCallParameter(0, PsiJavaPatterns.psiMethod()
                    .withName("find").definedInClass("org.apache.ofbiz.entity.Delegator")),

            PsiJavaPatterns.literalExpression().methodCallParameter(0, PsiJavaPatterns.psiMethod()
                    .withName("findOne").definedInClass("org.apache.ofbiz.entity.Delegator")),

            PsiJavaPatterns.literalExpression().methodCallParameter(0, PsiJavaPatterns.psiMethod()
                    .withName("findAll").definedInClass("org.apache.ofbiz.entity.Delegator")),

            PsiJavaPatterns.literalExpression().methodCallParameter(0, PsiJavaPatterns.psiMethod()
                    .withName("findCountByCondition").definedInClass("org.apache.ofbiz.entity.Delegator")),

            PsiJavaPatterns.literalExpression().methodCallParameter(0, PsiJavaPatterns.psiMethod()
                    .withName("findList").definedInClass("org.apache.ofbiz.entity.Delegator")),

            PsiJavaPatterns.literalExpression().methodCallParameter(1, PsiJavaPatterns.psiMethod()
                    .withName("addMemberEntity").definedInClass("org.apache.ofbiz.entity.model.DynamicViewEntity")),

            PsiJavaPatterns.literalExpression().methodCallParameter(1, PsiJavaPatterns.psiMethod().
                    withName("makeGenericValue").definedInClass("org.apache.ofbiz.entityext.data.EntityDataServices")),

            PsiJavaPatterns.literalExpression().methodCallParameter(0, PsiJavaPatterns.psiMethod()
                    .withName("from").definedInClass("org.apache.ofbiz.entity.util.EntityQuery"))
    )

    public static final PsiElementPattern JAVA_LABEL_CALL = PlatformPatterns.psiElement().andOr(
            PsiJavaPatterns.literalExpression().methodCallParameter(1, PsiJavaPatterns.psiMethod()
                    .withName("getMessage").definedInClass("org.apache.ofbiz.base.util.UtilProperties"))
    )

    // =============================================================
    //                      GROOVY
    // =============================================================
    public static final PsiElementPattern GROOVY_SERVICE_CALL = PlatformPatterns.psiElement().andOr(

            GroovyPatterns.groovyLiteralExpression()
                    .methodCallParameter(0,
                            GroovyPatterns.psiMethod().withName("runSync")
                                    .definedInClass("org.apache.ofbiz.service.LocalDispatcher")),

            GroovyPatterns.groovyLiteralExpression()
                    .methodCallParameter(0,
                            GroovyPatterns.psiMethod().withName("runAsync")
                                    .definedInClass("org.apache.ofbiz.service.LocalDispatcher")),

            GroovyPatterns.groovyLiteralExpression()
                    .methodCallParameter(0,
                            GroovyPatterns.psiMethod().withName("runAsyncWait")
                                    .definedInClass("org.apache.ofbiz.service.LocalDispatcher")),

            GroovyPatterns.groovyLiteralExpression()
                    .methodCallParameter(0,
                            GroovyPatterns.psiMethod().withName("runSyncIgnore")
                                    .definedInClass("org.apache.ofbiz.service.LocalDispatcher")),

            GroovyPatterns.groovyLiteralExpression()
                    .methodCallParameter(0,
                            GroovyPatterns.psiMethod().withName("runSyncNewTrans")
                                    .definedInClass("org.apache.ofbiz.service.LocalDispatcher")),

            GroovyPatterns.groovyLiteralExpression()
                    .methodCallParameter(0,
                            GroovyPatterns.psiMethod().withName("runService")),

            /**
             * TODO
             * Ce pattern ne fonctionne pas encore, à creuser dans les
             * GroovyPatterns.groovyScript()
             */
            GroovyPatterns.groovyLiteralExpression()
                    .methodCallParameter(0,
                            GroovyPatterns.psiMethod().withName("run")
                                    .definedInClass("org.apache.ofbiz.service.engine.GroovyBaseScript"))
    )

    public static final PsiElementPattern GROOVY_ENTITY_CALL = PlatformPatterns.psiElement().andOr(
            GroovyPatterns.groovyLiteralExpression().methodCallParameter(0,
                    GroovyPatterns.psiMethod().withName("find")
                            .definedInClass("org.apache.ofbiz.entity.Delegator")),

            GroovyPatterns.groovyLiteralExpression().methodCallParameter(0,
                    GroovyPatterns.psiMethod().withName("findOne")
                            .definedInClass("org.apache.ofbiz.entity.Delegator")),

            GroovyPatterns.groovyLiteralExpression().methodCallParameter(0,
                    GroovyPatterns.psiMethod().withName("findAll")
                            .definedInClass("org.apache.ofbiz.entity.Delegator")),

            // TODO : Marche pas
            GroovyPatterns.groovyLiteralExpression().methodCallParameter(0,
                    GroovyPatterns.psiMethod().withName("findCountByCondition")
                            .definedInClass("org.apache.ofbiz.entity.Delegator")),

            // TODO : Marche pas
            GroovyPatterns.groovyLiteralExpression().methodCallParameter(0,
                    GroovyPatterns.psiMethod().withName("findList")
                            .definedInClass("org.apache.ofbiz.entity.Delegator")),

            GroovyPatterns.groovyLiteralExpression().methodCallParameter(1,
                    GroovyPatterns.psiMethod().withName("addMemberEntity")
                            .definedInClass("org.apache.ofbiz.entity.model.DynamicViewEntity")),

            GroovyPatterns.groovyLiteralExpression().methodCallParameter(1,
                    GroovyPatterns.psiMethod().withName("makeGenericValue")
                            .definedInClass("org.apache.ofbiz.entityext.data.EntityDataServices")),

            GroovyPatterns.groovyLiteralExpression().methodCallParameter(0,
                    GroovyPatterns.psiMethod().withName("from")),

            // TODO : Marche pas
            GroovyPatterns.groovyLiteralExpression().methodCallParameter(0,
                    GroovyPatterns.psiMethod().withName("makeValue")
                            .definedInClass("org.apache.ofbiz.service.engine.GroovyBaseScript")),

            GroovyPatterns.groovyLiteralExpression().methodCallParameter(0,
                    GroovyPatterns.psiMethod().withName("findOne"))
    )

    public static final PsiElementPattern GROOVY_LABEL_CALL = PlatformPatterns.psiElement().andOr(
            GroovyPatterns.groovyLiteralExpression().methodCallParameter(1,
                    GroovyPatterns.psiMethod().withName("getMessage")
                            .definedInClass("org.apache.ofbiz.base.util.UtilProperties"))
    )

    // =============================================================
    //                      XML
    // =============================================================
    public static final XmlNamedElementPattern.XmlAttributePattern FORM_TARGET_PATTERN =
            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("form"))
                    .withName("target")

    public static final XmlNamedElementPattern.XmlAttributePattern RESPONSE_ATTR_PATTERN =
            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("response"))
                    .withName("value")

    public static final XmlNamedElementPattern.XmlAttributePattern ENTITY_NAME_ATTR_PATTERN =
            XmlPatterns.xmlAttribute()
                    .withName("entity", "entity-name", "default-entity-name", "rel-entity-name")

    public static final XmlNamedElementPattern.XmlAttributePattern SERVICE_NAME_ATTR_PATTERN = XmlPatterns.xmlAttribute().andOr(
            XmlPatterns.xmlAttribute()
                    .withName("service", "service-name"),
            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("event")
                            .withChild(XmlPatterns.xmlAttribute().withName("type")
                                    .withValue(XmlPatterns.string().contains("service")))
                    )
                    .withName("invoke")
    )

    public static final XmlNamedElementPattern.XmlAttributePattern JAVA_METHOD_ATTR_PATTERN = XmlPatterns.xmlAttribute().andOr(
            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("event")
                            .withChild(XmlPatterns.xmlAttribute().withName("type")
                                    .withValue(XmlPatterns.string().equalTo("java")))
                    )
                    .withName("invoke"),
            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("service")
                            .withChild(XmlPatterns.xmlAttribute().withName("engine")
                                    .withValue(XmlPatterns.string().equalTo("java")))
                    )
                    .withName("invoke")
    )
    
    public static final XmlNamedElementPattern.XmlAttributePattern PROPERTY_ATTR_PATTERN =
            XmlPatterns.xmlAttribute().withValue(
                    XmlPatterns.string().startsWith('${uiLabelMap.'))

    public static final XmlNamedElementPattern.XmlAttributePattern FORM_LOCATION_PATTERN = XmlPatterns.xmlAttribute().andOr(
            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("include-form"))
                    .withName("name"),
            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("include-grid"))
                    .withName("name"),
            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("form"))
                    .withName("extends"),
            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("grid"))
                    .withName("extends")
    )

    public static final XmlNamedElementPattern.XmlAttributePattern MENU_LOCATION_PATTERN = XmlPatterns.xmlAttribute().andOr(
            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("screenlet"))
                    .withName("navigation-menu-name"),
            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("include-menu"))
                    .withName("name")
    )

    public static final XmlNamedElementPattern.XmlAttributePattern FILE_LOCATION_ATTR_PATTERN =
            XmlPatterns.xmlAttribute()
                    .withName("entity-xml-url", "xml-resource", "extends-resource",
                            "resourceValue", "resource", "template", "page", "location", "image-location",
                            "component-location", "fallback-location", "default-fallback-location",
                            "default-location", "path")

    public static final XmlNamedElementPattern.XmlAttributePattern SCREEN_ATTR_PATTERN = XmlPatterns.xmlAttribute().andOr(
            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("include-screen"))
                    .withName("name"),
            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("view-map")
                            .withChild(XmlPatterns.xmlAttribute().withName("type").withValue(
                                    XmlPatterns.string().equalTo("screen"))))
                    .withName("page")
    )
}
