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

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrVariable
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression

import java.util.regex.Matcher
import java.util.regex.Pattern

class EntityFieldNameCompletionProvider extends CompletionProvider<CompletionParameters> {
    private static final Logger LOG = Logger.getInstance(EntityFieldNameCompletionProvider.class)


    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
        PsiElement element = parameters.getPosition()
        try {
            PsiElement genericValueRef = element.getParent().getFirstChild()
            assert genericValueRef instanceof GrReferenceExpression

            PsiElement initialVariable = genericValueRef.resolve()
            assert initialVariable instanceof GrVariable

            String declarationExpr = initialVariable.getInitializerGroovy().getText()

            final Pattern ENTITY_NAME_PATTERN = Pattern.compile("(['\"](.*?)['\"])")
            Matcher matcher = ENTITY_NAME_PATTERN.matcher(declarationExpr)

            String entityName = matcher.find() ? matcher.group(0) : null

            String str = 'trolo'
        } catch (Exception e) {
            LOG.info(e)
        }
        LookupElement el = LookupElementBuilder.create('GENERIC_VALUE_ATTRIBUTE_TROLO')
        result.addElement(PrioritizedLookupElement.withPriority(el, 3000))
    }
}
