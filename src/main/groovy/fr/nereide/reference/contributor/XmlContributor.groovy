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

package fr.nereide.reference.contributor


import com.intellij.patterns.XmlNamedElementPattern.XmlAttributePattern
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar
import fr.nereide.reference.xml.ControllerRequestReferenceProvider
import fr.nereide.reference.xml.ControllerViewReferenceProvider
import fr.nereide.reference.xml.EntityReferenceProvider
import fr.nereide.reference.xml.FileReferenceProvider
import fr.nereide.reference.xml.FormReferenceProvider
import fr.nereide.reference.xml.JavaMethodReferenceProvider
import fr.nereide.reference.xml.MenuReferenceProvider
import fr.nereide.reference.xml.ScreenReferenceProvider
import fr.nereide.reference.xml.ServiceReferenceProvider
import fr.nereide.reference.xml.UiLabelReferenceProvider
import org.jetbrains.annotations.NotNull

class XmlContributor extends PsiReferenceContributor {
    XmlContributor() {}

    void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(FORM_TARGET_PATTERN), new ControllerRequestReferenceProvider()
        )
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(RESPONSE_ATTR_PATTERN), new ControllerViewReferenceProvider()
        )
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(ENTITY_NAME_ATTR_PATTERN), new EntityReferenceProvider()
        )
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(SERVICE_NAME_ATTR_PATTERN), new ServiceReferenceProvider()
        )
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(PROPERTY_ATTR_PATTERN), new UiLabelReferenceProvider()
        )
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(FORM_LOCATION_PATTERN), new FormReferenceProvider()
        )
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(FILE_LOCATION_ATTR_PATTERN), new FileReferenceProvider()
        )
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(SCREEN_ATTR_PATTERN), new ScreenReferenceProvider()
        )
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(MENU_LOCATION_PATTERN), new MenuReferenceProvider()
        )
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(JAVA_METHOD_ATTR_PATTERN), new JavaMethodReferenceProvider()
        )

    }

    // =============================================================
    //                      PATTERNS
    // =============================================================

    public static final XmlAttributePattern FORM_TARGET_PATTERN =
            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("form"))
                    .withName("target")

    public static final XmlAttributePattern RESPONSE_ATTR_PATTERN =
            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("response"))
                    .withName("value")

    public static final XmlAttributePattern ENTITY_NAME_ATTR_PATTERN =
            XmlPatterns.xmlAttribute()
                    .withName("entity", "entity-name", "default-entity-name", "rel-entity-name")

    public static final XmlAttributePattern SERVICE_NAME_ATTR_PATTERN = XmlPatterns.xmlAttribute().andOr(
            XmlPatterns.xmlAttribute()
                    .withName("service", "service-name"),
            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("event")
                            .withChild(XmlPatterns.xmlAttribute().withName("type")
                                    .withValue(XmlPatterns.string().contains("service")))
                    )
                    .withName("invoke")
    )

    public static final XmlAttributePattern JAVA_METHOD_ATTR_PATTERN = XmlPatterns.xmlAttribute().andOr(
            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("event")
                            .withChild( XmlPatterns.xmlAttribute().withName("type")
                                    .withValue(XmlPatterns.string().equalTo("java")))
                    )
                    .withName("invoke"),
            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("service")
                            .withChild( XmlPatterns.xmlAttribute().withName("engine")
                                    .withValue(XmlPatterns.string().equalTo("java")))
                    )
                    .withName("invoke")
    )

    public static final XmlAttributePattern PROPERTY_ATTR_PATTERN =
            XmlPatterns.xmlAttribute().withValue(
                    XmlPatterns.string().startsWith('${uiLabelMap.'))

    public static final XmlAttributePattern FORM_LOCATION_PATTERN = XmlPatterns.xmlAttribute().andOr(
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

    public static final XmlAttributePattern MENU_LOCATION_PATTERN = XmlPatterns.xmlAttribute().andOr(
            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("screenlet"))
                    .withName("navigation-menu-name"),

            XmlPatterns.xmlAttribute()
                    .withParent(XmlPatterns.xmlTag().withName("include-menu"))
                    .withName("name")
    )

    public static final XmlAttributePattern FILE_LOCATION_ATTR_PATTERN =
            XmlPatterns.xmlAttribute()
                    .withName("entity-xml-url", "xml-resource", "extends-resource",
                            "resourceValue", "resource", "template", "page", "location", "image-location",
                            "component-location", "fallback-location", "default-fallback-location",
                            "default-location", "path")

    public static final XmlAttributePattern SCREEN_ATTR_PATTERN = XmlPatterns.xmlAttribute().andOr(
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
