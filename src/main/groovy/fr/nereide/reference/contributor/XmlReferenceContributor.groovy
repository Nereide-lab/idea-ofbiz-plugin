/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * 'License') you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * 'AS IS' BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package fr.nereide.reference.contributor

import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar
import fr.nereide.project.pattern.OfbizXmlPatterns
import fr.nereide.reference.common.provider.EntityReferenceProvider
import fr.nereide.reference.common.provider.ServiceReferenceProvider
import fr.nereide.reference.common.provider.UiLabelReferenceProvider
import fr.nereide.reference.xml.provider.DatasourceReferenceProvider
import fr.nereide.reference.xml.provider.FileReferenceProvider
import fr.nereide.reference.xml.provider.FormReferenceProvider
import fr.nereide.reference.xml.provider.GridReferenceProvider
import fr.nereide.reference.xml.provider.GroovyServiceMethodReferenceProvider
import fr.nereide.reference.xml.provider.JavaMethodReferenceProvider
import fr.nereide.reference.xml.provider.MenuReferenceProvider
import fr.nereide.reference.xml.provider.RequestMapReferenceProvider
import fr.nereide.reference.xml.provider.ScreenReferenceProvider
import fr.nereide.reference.xml.provider.ServiceEngineReferenceProvider
import fr.nereide.reference.xml.provider.ViewMapReferenceProvider
import org.jetbrains.annotations.NotNull

/**
 * General Groovy contributor that accepts custom references
 */
class XmlReferenceContributor extends PsiReferenceContributor {

    void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.with {
            registerReferenceProvider(OfbizXmlPatterns.URI_CALL,
                    new RequestMapReferenceProvider())
            registerReferenceProvider(OfbizXmlPatterns.RESPONSE_CALL,
                    new ViewMapReferenceProvider())
            registerReferenceProvider(OfbizXmlPatterns.ENTITY_OR_VIEW_CALL,
                    new EntityReferenceProvider())
            registerReferenceProvider(OfbizXmlPatterns.SERVICE_DEF_CALL,
                    new ServiceReferenceProvider())
            registerReferenceProvider(OfbizXmlPatterns.LABEL_CALL,
                    new UiLabelReferenceProvider())
            registerReferenceProvider(OfbizXmlPatterns.FORM_CALL,
                    new FormReferenceProvider())
            registerReferenceProvider(OfbizXmlPatterns.GRID_CALL,
                    new GridReferenceProvider())
            registerReferenceProvider(OfbizXmlPatterns.FILE_CALL,
                    new FileReferenceProvider())
            registerReferenceProvider(OfbizXmlPatterns.SCREEN_CALL,
                    new ScreenReferenceProvider())
            registerReferenceProvider(OfbizXmlPatterns.MENU_CALL,
                    new MenuReferenceProvider())
            registerReferenceProvider(OfbizXmlPatterns.JAVA_EVENT_CALL,
                    new JavaMethodReferenceProvider())
            registerReferenceProvider(OfbizXmlPatterns.GROOVY_SERVICE_METHOD,
                    new GroovyServiceMethodReferenceProvider())
            registerReferenceProvider(OfbizXmlPatterns.DATASOURCE_CALL,
                    new DatasourceReferenceProvider())
            registerReferenceProvider(OfbizXmlPatterns.SERVICE_ENGINE_CALL,
                    new ServiceEngineReferenceProvider())
        }
    }

}
