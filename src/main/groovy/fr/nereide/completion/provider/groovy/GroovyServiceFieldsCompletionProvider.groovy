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
package fr.nereide.completion.provider.groovy

import static fr.nereide.project.pattern.OfbizPluginConstants.DEFAULT_COMPLETION_PRIORITY

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.utils.PsiUtils
import fr.nereide.project.worker.ServiceWorker
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrVariable
import org.jetbrains.plugins.groovy.lang.psi.api.statements.arguments.GrNamedArgument
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrAssignmentExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral

/**
 * Part of the OFBiz plugin Completion system
 */
class GroovyServiceFieldsCompletionProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context,
                                  @NotNull CompletionResultSet result) {

        if (PluginActivator.getInstance(parameters.position.project).inactive) return
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(parameters.position.project)
        String serviceName = extractServiceNameFromCall(parameters.position)
        if (!serviceName) return

        ServiceWorker.getServiceOutParameters(ph, serviceName, ServiceWorker.FALSE).forEach { attr ->
            LookupElement lookupElement = LookupElementBuilder.create(attr.('attribute-name'))
                    .withTailText(' (Optional: false)', true)
            result.addElement(PrioritizedLookupElement.withPriority(lookupElement, DEFAULT_COMPLETION_PRIORITY))
        }
        ServiceWorker.getServiceOutParameters(ph, serviceName, ServiceWorker.TRUE).forEach { attr ->
            LookupElement lookupElement = LookupElementBuilder.create(attr.('attribute-name'))
                    .withTailText(' (Optional: true)', true)
            result.addElement(PrioritizedLookupElement.withPriority(lookupElement, DEFAULT_COMPLETION_PRIORITY))
        }
    }

    private static String extractServiceNameFromCall(PsiElement element) {
        String serviceName
        try {
            GrReferenceExpression qualifier = (element.parent as GrReferenceExpression)
                    .qualifier as GrReferenceExpression
            GrVariable serviceResult = qualifier.resolve() as GrVariable
            List<GrAssignmentExpression> assignments = PsiUtils.getGroovyVariableAssignements(serviceResult)
            GrMethodCall serviceCall
            if (assignments) {
                GrAssignmentExpression lastAssignment = assignments.findAll { expr ->
                    expr.textOffset < element.textOffset
                }.last()
                serviceCall = PsiTreeUtil.findChildOfType(lastAssignment, GrMethodCall)
            } else {
                GrExpression initializer = (serviceResult as GrVariable).initializerGroovy
                serviceCall = initializer as GrMethodCall
            }

            if (serviceCall?.invokedExpression?.text != 'run') return null
            GrNamedArgument serviceArg = serviceCall.namedArguments.find { arg -> arg.labelName == 'service' }
            if (!serviceArg || !(serviceArg.expression instanceof GrLiteral)) {
                return null
            }
            serviceName = serviceArg.expression.value?.toString()
        } catch (ClassCastException ignored) {
            return null
        }

        return serviceName
    }

}
