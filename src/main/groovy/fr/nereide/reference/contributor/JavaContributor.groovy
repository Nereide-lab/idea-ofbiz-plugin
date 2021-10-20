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

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.patterns.PsiJavaPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.util.ProcessingContext
import fr.nereide.reference.java.EntityJavaReference
import fr.nereide.reference.java.ServiceJavaReference
import fr.nereide.reference.java.UiLabelJavaReference
import org.jetbrains.annotations.NotNull

class JavaContributor extends PsiReferenceContributor {
    JavaContributor() {}

    void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(SERVICE_CALL, new PsiReferenceProvider() {
            @NotNull
            PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                PsiLiteralExpression el = (PsiLiteralExpression) element
                ServiceJavaReference service = new ServiceJavaReference(el, true)
                PsiReference[] reference = (PsiReference) service
                return reference
            }
        })
        registrar.registerReferenceProvider(ENTITY_CALL, new PsiReferenceProvider() {
            @NotNull
            PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                PsiLiteralExpression el = (PsiLiteralExpression) element
                EntityJavaReference entity = new EntityJavaReference(el, true)
                PsiReference[] reference = (PsiReference) entity
                return reference
            }
        })
        registrar.registerReferenceProvider(LABEL_REFERENCE, new PsiReferenceProvider() {
            @NotNull
            PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                PsiLiteralExpression el = (PsiLiteralExpression) element
                UiLabelJavaReference label = new UiLabelJavaReference(el, true)
                PsiReference[] reference = (PsiReference) label
                return reference
            }
        })
    }

    // =============================================================
    //                      PATTERNS
    // =============================================================

    /**
     * Les patterns en java regardent quelle classe appelle quelle methode.
     * On regarde spécifie aussi à quelle index de l'appel de methode se trouve
     * l'élément dont on souhaite la référence
     */
    static final PsiElementPattern SERVICE_CALL = PlatformPatterns.psiElement().andOr(
            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("makeValidContext")
                            .definedInClass("fr.nereide.service.DispatchContext"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runSync")
                            .definedInClass("fr.nereide.service.LocalDispatcher"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runAsync")
                            .definedInClass("fr.nereide.service.LocalDispatcher"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runAsyncWait")
                            .definedInClass("fr.nereide.service.LocalDispatcher"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runSyncIgnore")
                            .definedInClass("fr.nereide.service.LocalDispatcher"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runSyncNewTrans")
                            .definedInClass("fr.nereide.service.LocalDispatcher"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runService")
                            .definedInClass("fr.nereide.base.util.ScriptHelper"))),

            PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("makeValidContext")
                            .definedInClass("fr.nereide.service.DispatchContext")),

            PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runSync")
                            .definedInClass("fr.nereide.service.LocalDispatcher")),

            PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runAsync")
                            .definedInClass("fr.nereide.service.LocalDispatcher")),

            PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runAsyncWait")
                            .definedInClass("fr.nereide.service.LocalDispatcher")),

            PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runSyncIgnore")
                            .definedInClass("fr.nereide.service.LocalDispatcher")),

            PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runSyncNewTrans")
                            .definedInClass("fr.nereide.service.LocalDispatcher")),

            PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("runService")
                            .definedInClass("fr.nereide.base.util.ScriptHelper"))
    )

    public static final PsiElementPattern ENTITY_CALL = PlatformPatterns.psiElement().andOr(
            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("find")
                            .definedInClass("fr.nereide.entity.Delegator"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("findOne")
                            .definedInClass("fr.nereide.entity.Delegator"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("findAll")
                            .definedInClass("fr.nereide.entity.Delegator"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("findCountByCondition")
                            .definedInClass("fr.nereide.entity.Delegator"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("findList")
                            .definedInClass("fr.nereide.entity.Delegator"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(1, PsiJavaPatterns.psiMethod()
                            .withName("addMemberEntity")
                            .definedInClass("fr.nereide.entity.model.DynamicViewEntity"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(1, PsiJavaPatterns.psiMethod()
                            .withName("makeGenericValue")
                            .definedInClass("fr.nereide.entityext.data.EntityDataServices"))),

            PlatformPatterns.psiElement().withTreeParent(PsiJavaPatterns.literalExpression()
                    .methodCallParameter(0, PsiJavaPatterns.psiMethod()
                            .withName("from")
                            .definedInClass("fr.nereide.entity.util.EntityQuery"))),

            PsiJavaPatterns.literalExpression().methodCallParameter(0, PsiJavaPatterns.psiMethod()
                    .withName("find").definedInClass("fr.nereide.entity.Delegator")),

            PsiJavaPatterns.literalExpression().methodCallParameter(0, PsiJavaPatterns.psiMethod()
                    .withName("findOne").definedInClass("fr.nereide.entity.Delegator")),

            PsiJavaPatterns.literalExpression().methodCallParameter(0, PsiJavaPatterns.psiMethod()
                    .withName("findAll").definedInClass("fr.nereide.entity.Delegator")),

            PsiJavaPatterns.literalExpression().methodCallParameter(0, PsiJavaPatterns.psiMethod()
                    .withName("findCountByCondition").definedInClass("fr.nereide.entity.Delegator")),

            PsiJavaPatterns.literalExpression().methodCallParameter(0, PsiJavaPatterns.psiMethod()
                    .withName("findList").definedInClass("fr.nereide.entity.Delegator")),

            PsiJavaPatterns.literalExpression().methodCallParameter(1, PsiJavaPatterns.psiMethod()
                    .withName("addMemberEntity").definedInClass("fr.nereide.entity.model.DynamicViewEntity")),

            PsiJavaPatterns.literalExpression().methodCallParameter(1, PsiJavaPatterns.psiMethod().
                    withName("makeGenericValue").definedInClass("fr.nereide.entityext.data.EntityDataServices")),

            PsiJavaPatterns.literalExpression().methodCallParameter(0, PsiJavaPatterns.psiMethod()
                    .withName("from").definedInClass("fr.nereide.entity.util.EntityQuery"))
    )

    public static final PsiElementPattern LABEL_REFERENCE = PlatformPatterns.psiElement().andOr(
            PsiJavaPatterns.literalExpression().methodCallParameter(1, PsiJavaPatterns.psiMethod()
                    .withName("getMessage").definedInClass("fr.nereide.base.util.UtilProperties"))
    )

}
