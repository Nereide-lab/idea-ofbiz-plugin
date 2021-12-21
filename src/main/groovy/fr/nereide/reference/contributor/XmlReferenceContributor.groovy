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


import com.intellij.patterns.XmlPatterns
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar
import fr.nereide.project.OfbizPatterns
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

class XmlReferenceContributor extends PsiReferenceContributor {
    XmlReferenceContributor() {}

    void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(OfbizPatterns.FORM_TARGET_PATTERN), new ControllerRequestReferenceProvider()
        )
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(OfbizPatterns.RESPONSE_ATTR_PATTERN), new ControllerViewReferenceProvider()
        )
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(OfbizPatterns.ENTITY_NAME_ATTR_PATTERN), new EntityReferenceProvider()
        )
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(OfbizPatterns.SERVICE_NAME_ATTR_PATTERN), new ServiceReferenceProvider()
        )
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(OfbizPatterns.PROPERTY_ATTR_PATTERN), new UiLabelReferenceProvider()
        )
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(OfbizPatterns.FORM_LOCATION_PATTERN), new FormReferenceProvider()
        )
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(OfbizPatterns.FILE_LOCATION_ATTR_PATTERN), new FileReferenceProvider()
        )
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(OfbizPatterns.SCREEN_ATTR_PATTERN), new ScreenReferenceProvider()
        )
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(OfbizPatterns.MENU_LOCATION_PATTERN), new MenuReferenceProvider()
        )
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue()
                .withParent(OfbizPatterns.JAVA_METHOD_ATTR_PATTERN), new JavaMethodReferenceProvider()
        )
    }
}
