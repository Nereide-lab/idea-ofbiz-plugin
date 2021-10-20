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
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.util.ProcessingContext
import fr.nereide.reference.groovy.EntityGroovyReference
import fr.nereide.reference.groovy.ServiceGroovyReference
import fr.nereide.reference.groovy.UiLabelGroovyReference
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral
import org.jetbrains.plugins.groovy.lang.psi.patterns.GroovyPatterns

class GroovyContributor extends PsiReferenceContributor {
    GroovyContributor() {}

    void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(GROOVY_SERVICE_CALL, new PsiReferenceProvider() {
            @NotNull
            PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                GrLiteral el = (GrLiteral) element
                ServiceGroovyReference service = new ServiceGroovyReference(el, true)
                PsiReference[] reference = (PsiReference) service
                return reference
            }
        })
        registrar.registerReferenceProvider(GROOVY_ENTITY_CALL, new PsiReferenceProvider() {
            @NotNull
            PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                GrLiteral el = (GrLiteral) element
                EntityGroovyReference entity = new EntityGroovyReference(el, true)
                PsiReference[] reference = (PsiReference) entity
                return reference
            }
        })
        registrar.registerReferenceProvider(GROOVY_LABEL_CALL, new PsiReferenceProvider() {
            @NotNull
            PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                GrLiteral el = (GrLiteral) element
                UiLabelGroovyReference label = new UiLabelGroovyReference(el, true)
                PsiReference[] reference = (PsiReference) label
                return reference
            }
        })
    }

    // =============================================================
    //                      PATTERNS
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

}
