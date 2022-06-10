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


import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.util.ProcessingContext
import fr.nereide.project.OfbizPatterns
import fr.nereide.reference.java.EntityJavaReference
import fr.nereide.reference.java.ServiceJavaReference
import fr.nereide.reference.java.UiLabelJavaReference
import org.jetbrains.annotations.NotNull

class JavaReferenceContributor extends PsiReferenceContributor {
    JavaReferenceContributor() {}

    void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(OfbizPatterns.JAVA.SERVICE_CALL, new PsiReferenceProvider() {
            @NotNull
            PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                PsiLiteralExpression el = (PsiLiteralExpression) element
                ServiceJavaReference service = new ServiceJavaReference(el, true)
                PsiReference[] reference = (PsiReference) service
                return reference
            }
        })
        registrar.registerReferenceProvider(OfbizPatterns.JAVA.ENTITY_CALL, new PsiReferenceProvider() {
            @NotNull
            PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                PsiLiteralExpression el = (PsiLiteralExpression) element
                EntityJavaReference entity = new EntityJavaReference(el, true)
                PsiReference[] reference = (PsiReference) entity
                return reference
            }
        })
        registrar.registerReferenceProvider(OfbizPatterns.JAVA.LABEL_CALL, new PsiReferenceProvider() {
            @NotNull
            PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                PsiLiteralExpression el = (PsiLiteralExpression) element
                UiLabelJavaReference label = new UiLabelJavaReference(el, true)
                PsiReference[] reference = (PsiReference) label
                return reference
            }
        })
    }
}
