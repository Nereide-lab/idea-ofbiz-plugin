package org.apache.ofbiz.reference.contributor

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
import org.apache.ofbiz.reference.java.EntityJavaReference
import org.apache.ofbiz.reference.java.ServiceJavaReference
import org.apache.ofbiz.reference.java.UiLabelJavaReference
import org.jetbrains.annotations.NotNull

class JavaContributor extends PsiReferenceContributor {
    JavaContributor() {}

    void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(SERVICE_CALL, new PsiReferenceProvider() {
            @NotNull
            PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                PsiLiteralExpression el = (PsiLiteralExpression)element
                ServiceJavaReference service = new ServiceJavaReference(el, true)
                PsiReference[] reference = (PsiReference) service
                return reference
            }
        })
        registrar.registerReferenceProvider(ENTITY_CALL, new PsiReferenceProvider() {
            @NotNull
            PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                PsiLiteralExpression el = (PsiLiteralExpression)element
                EntityJavaReference entity = new EntityJavaReference(el, true)
                PsiReference[] reference = (PsiReference) entity
                return reference
            }
        })
        registrar.registerReferenceProvider(LABEL_REFERENCE, new PsiReferenceProvider() {
            @NotNull
            PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                PsiLiteralExpression el = (PsiLiteralExpression)element
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

    public static final PsiElementPattern ENTITY_CALL = PlatformPatterns.psiElement().andOr(
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

    public static final PsiElementPattern LABEL_REFERENCE = PlatformPatterns.psiElement().andOr(
            PsiJavaPatterns.literalExpression().methodCallParameter(1, PsiJavaPatterns.psiMethod()
                    .withName("getMessage").definedInClass("org.apache.ofbiz.base.util.UtilProperties"))
    )

}
