/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package fr.nereide.completion.provider.common

import static com.intellij.psi.util.PsiTreeUtil.getChildrenOfType
import static com.intellij.psi.util.PsiTreeUtil.getParentOfType
import static fr.nereide.project.pattern.OfbizPluginConstants.DYNAMIC_VIEW_ENTITY_CLASS_NAME
import static fr.nereide.project.worker.EntityWorker.getEntityFields
import static fr.nereide.project.worker.EntityWorker.getViewFields

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiType
import com.intellij.psi.PsiVariable
import com.intellij.usageView.UsageInfo
import com.intellij.util.ProcessingContext
import fr.nereide.dom.element.entitymodel.Entity
import fr.nereide.dom.element.entitymodel.ViewEntity
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.pattern.OfbizPluginConstants
import fr.nereide.project.utils.PsiUtils
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrVariable
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression
import org.jetbrains.plugins.groovy.lang.psi.dataFlow.types.TypeInferenceHelper

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Part of the OFBiz plugin completion system
 */
abstract class EntityFieldCompletionProvider extends CompletionProvider<CompletionParameters> {

    private static final Pattern ENTITY_NAME_PATTERN = Pattern.compile("(['\"](.*?)['\"])")
    public static final String DBLE_QUOTE = '\"'

    /**
     * Extracts entity name from declarations like
     * <code> EntityQuery.use(delegator).from() </code>
     * @param declaration the declaration string
     * @return the entity name
     */
    static String getEntityNameFromDeclarationString(String declaration) {
        Matcher matcher = ENTITY_NAME_PATTERN.matcher(declaration)
        String entityName = matcher.find() ? matcher.group(0) : null
        return entityName ? entityName.substring(1, entityName.length() - 1) : null
    }

    /**
     * Add lookup element to display, with the fields and the entity name at the end of the line
     * @param result the result set to be completed
     * @param fields the fields of the entity
     * @param entityName the entity name
     */
    static void addLookupElementFromEntity(CompletionResultSet result, List<String> fields, String entityName) {
        fields.forEach { field ->
            result.addElement(PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create(field).withTypeText("From $entityName" as String),
                    1000))
        }
    }

    /**
     * Searchs for the entity name in the query String
     */
    static String getEntityNameFromQuery(PsiElement element, Class methodTypeClass) {
        PsiElement originMethod = getParentOfType(element, methodTypeClass)
        if (originMethod && originMethod.text.contains(OfbizPluginConstants.QUERY_FROM_STATEMENT)) {
            return getEntityNameFromDeclarationString(originMethod.text)
        }
        return null
    }

    static boolean variableHasDveType(PsiVariable dveVariable, PsiElement dveMethodCall) {
        if (dveVariable && dveVariable.typeElement) {
            return dveVariable.typeElement.text == DYNAMIC_VIEW_ENTITY_CLASS_NAME
        } else if (dveVariable instanceof GrVariable) {
            GrReferenceExpression methodCallRMember = getChildrenOfType(dveMethodCall, GrReferenceExpression)[0]
            if (!methodCallRMember) return false
            GrReferenceExpression dveVarExpr = getChildrenOfType(methodCallRMember, GrReferenceExpression)[0]
            if (!dveVarExpr) return false
            PsiType type = TypeInferenceHelper.getInferredType(dveVarExpr)
            return type && type.presentableText == DYNAMIC_VIEW_ENTITY_CLASS_NAME
        }
        return false
    }

    /**
     * Tries to extract the entity name from the context and potentials assignements
     */
    String getEntityNameFromLastQueryAssignment(PsiVariable genericValueVariable) {
        List<UsageInfo> usages = PsiUtils.getUsagesOfVariable(genericValueVariable)
        if (!usages) return ''
        UsageInfo lastQuery = usages.stream().filter { usage ->
            PsiElement assign = getParentOfType(usage.element, assigmentClass)
            assign && getAssigmentString(assign).contains(OfbizPluginConstants.QUERY_FROM_STATEMENT)
        }.toList()?.last()
        if (!lastQuery) return ''
        PsiElement lastAssignExpr = getParentOfType(lastQuery.element, assigmentClass)
        return getEntityNameFromDeclarationString(getAssigmentString(lastAssignExpr))
    }

    /**
     * Tries to retrieve the entity name from the context Dynamic view
     * @param addAliasInitialMethod addAlias method where the completion takes place
     * @param initialDve dve variable
     * @param index index of the alias maram to use for entityName
     * @return the entity name
     */
    String getEntityNameFromDynamicView(PsiElement addAliasInitialMethod, PsiVariable initialDve, int index) {
        PsiElement[] params = getMethodArgs(addAliasInitialMethod)
        if (params) {
            String aliasToLookFor = params[index].text
            List<UsageInfo> dveUsages = PsiUtils.getUsagesOfVariable(initialDve)
            PsiElement relevantAddAlias = dveUsages.collect { UsageInfo usage ->
                getParentOfType(usage.element, methodExprClass)
            }.find { PsiElement addAliasCall ->
                addAliasMethodUsesWantedAlias(addAliasCall, aliasToLookFor)
            }
            if (!relevantAddAlias) return null
            return getMethodArgs(relevantAddAlias)?[1].text
        }
        return null
    }

    String getEntityNameFromDynamicView(PsiElement addAliasInitialMethod, PsiVariable initialDve) {
        return getEntityNameFromDynamicView(addAliasInitialMethod, initialDve, 0)
    }

    boolean addAliasMethodUsesWantedAlias(PsiElement addAliasCall, String aliasToLookFor) {
        PsiElement[] params = getMethodArgs(addAliasCall)
        String aliasParam = params?[0].text
        return aliasParam && aliasToLookFor == aliasParam
    }

    String getEntityNameFromInsideDynamisView(PsiElement element, Class methodClass) {
        PsiElement dveMethodCall = getParentOfType(element, methodClass)
        PsiVariable dveVariable = PsiUtils.getPsiTopVariable(dveMethodCall, referenceExpressionClass)
        if (dveVariable && variableHasDveType(dveVariable, dveMethodCall)) {
            return getEntityNameFromDynamicView(dveMethodCall, dveVariable)
        }
        return null
    }

    /**
     * Searches the EntityName from a modelMeymap in a Dynamic view
     */
    String getEntityNameFromKeyMapInDve(PsiElement element, Class methodClass, int index) {
        PsiElement dveMethodCall = getParentOfType(
                getParentOfType(element, methodClass),
                methodClass)
        PsiVariable dveVariable = PsiUtils.getPsiTopVariable(dveMethodCall, referenceExpressionClass)
        return dveVariable ? getEntityNameFromDynamicView(dveMethodCall, dveVariable, index) : null
    }

    /**
     * Try to get the EntityName from PsiElement and its context
     */
    abstract String getEntityNameFromPsiElement(PsiElement psiElement)

    /**
     * get the class to use for assigment search in psi tree when looking for assignement
     */
    abstract Class getAssigmentClass()

    /**
     * Returns the PsiClass used for reference expressions
     */
    abstract Class getReferenceExpressionClass()

    /**
     * returns the right operator of the assignement depending if the variable is java or groovy
     */
    abstract String getAssigmentString(PsiElement assign)

    /**
     * Returns a method arguments depending on language
     */
    abstract PsiElement[] getMethodArgs(PsiElement method)

    /**
     * Returns the PsiClass used for Method psi representations
     */
    abstract Class getMethodExprClass()

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context,
                                  @NotNull CompletionResultSet result) {
        if (PluginActivator.getInstance(parameters.position.project).inactive) return
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(parameters.position.project)
        String entityName
        try {
            entityName = getEntityNameFromPsiElement(parameters.position)
        } catch (Exception ignored) { // codenarc-disable CatchException
            return
        }
        if (!entityName) return
        if (entityName.startsWith(DBLE_QUOTE)) entityName = entityName.replaceAll(DBLE_QUOTE, '')
        Entity entity = ph.getEntity(entityName)

        if (entity) {
            List<String> entityFields = getEntityFields(entity, ph)
            addLookupElementFromEntity(result, entityFields, entityName)
        } else {
            ViewEntity view = ph.getViewEntity(entityName)
            List<String> viewFields = getViewFields(view, ph, 0)
            addLookupElementFromEntity(result, viewFields, entityName)
        }
    }

}
