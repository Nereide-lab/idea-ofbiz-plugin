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


import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar
import fr.nereide.project.OfbizPatterns
import fr.nereide.reference.xml.GroovyServiceMethodReferenceProvider
import fr.nereide.reference.xml.RequestMapReferenceProvider
import fr.nereide.reference.xml.ViewMapReferenceProvider
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
        registrar.registerReferenceProvider(OfbizPatterns.XML.URI_CALL, new RequestMapReferenceProvider())
        registrar.registerReferenceProvider(OfbizPatterns.XML.RESPONSE_CALL, new ViewMapReferenceProvider())
        registrar.registerReferenceProvider(OfbizPatterns.XML.ENTITY_CALL, new EntityReferenceProvider())
        registrar.registerReferenceProvider(OfbizPatterns.XML.SERVICE_DEF_CALL, new ServiceReferenceProvider())
        registrar.registerReferenceProvider(OfbizPatterns.XML.LABEL_CALL, new UiLabelReferenceProvider())
        registrar.registerReferenceProvider(OfbizPatterns.XML.FORM_CALL, new FormReferenceProvider())
        registrar.registerReferenceProvider(OfbizPatterns.XML.FILE_CALL, new FileReferenceProvider())
        registrar.registerReferenceProvider(OfbizPatterns.XML.SCREEN_CALL, new ScreenReferenceProvider())
        registrar.registerReferenceProvider(OfbizPatterns.XML.MENU_CALL, new MenuReferenceProvider())
        registrar.registerReferenceProvider(OfbizPatterns.XML.JAVA_EVENT_CALL, new JavaMethodReferenceProvider())
        registrar.registerReferenceProvider(OfbizPatterns.XML.GROOVY_SERVICE_METHOD, new GroovyServiceMethodReferenceProvider())
    }
}
