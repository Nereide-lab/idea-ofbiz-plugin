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
import fr.nereide.project.pattern.OfbizJavaPatterns
import fr.nereide.reference.common.provider.EntityReferenceProvider
import fr.nereide.reference.common.provider.ServiceReferenceProvider
import fr.nereide.reference.common.provider.UiLabelReferenceProvider

class JavaReferenceContributor extends PsiReferenceContributor {
    JavaReferenceContributor() {}

    void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(OfbizJavaPatterns.SERVICE_CALL, new ServiceReferenceProvider())
        registrar.registerReferenceProvider(OfbizJavaPatterns.ENTITY_CALL, new EntityReferenceProvider())
        registrar.registerReferenceProvider(OfbizJavaPatterns.LABEL_CALL, new UiLabelReferenceProvider())
    }
}
