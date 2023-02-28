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
import fr.nereide.project.pattern.OfbizXmlPatterns
import fr.nereide.reference.common.provider.EntityReferenceProvider
import fr.nereide.reference.common.provider.ServiceReferenceProvider
import fr.nereide.reference.common.provider.UiLabelReferenceProvider
import fr.nereide.reference.xml.provider.*
import org.jetbrains.annotations.NotNull

class XmlReferenceContributor extends PsiReferenceContributor {
    XmlReferenceContributor() {}

    void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(OfbizXmlPatterns.URI_CALL, new RequestMapReferenceProvider())
        registrar.registerReferenceProvider(OfbizXmlPatterns.RESPONSE_CALL, new ViewMapReferenceProvider())
        registrar.registerReferenceProvider(OfbizXmlPatterns.ENTITY_CALL, new EntityReferenceProvider())
        registrar.registerReferenceProvider(OfbizXmlPatterns.SERVICE_DEF_CALL, new ServiceReferenceProvider())
        registrar.registerReferenceProvider(OfbizXmlPatterns.LABEL_CALL, new UiLabelReferenceProvider())
        registrar.registerReferenceProvider(OfbizXmlPatterns.FORM_CALL, new FormReferenceProvider())
        registrar.registerReferenceProvider(OfbizXmlPatterns.GRID_CALL, new GridReferenceProvider())
        registrar.registerReferenceProvider(OfbizXmlPatterns.FILE_CALL, new FileReferenceProvider())
        registrar.registerReferenceProvider(OfbizXmlPatterns.SCREEN_CALL, new ScreenReferenceProvider())
        registrar.registerReferenceProvider(OfbizXmlPatterns.MENU_CALL, new MenuReferenceProvider())
        registrar.registerReferenceProvider(OfbizXmlPatterns.JAVA_EVENT_CALL, new JavaMethodReferenceProvider())
        registrar.registerReferenceProvider(OfbizXmlPatterns.GROOVY_SERVICE_METHOD, new GroovyServiceMethodReferenceProvider())
        registrar.registerReferenceProvider(OfbizXmlPatterns.DATASOURCE_CALL, new DatasourceReferenceProvider())
    }
}
