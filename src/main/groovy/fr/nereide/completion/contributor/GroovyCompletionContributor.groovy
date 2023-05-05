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

package fr.nereide.completion.contributor

import com.intellij.codeInsight.completion.CompletionType
import fr.nereide.completion.provider.groovy.GroovyEntityFieldCompletionProvider

import static fr.nereide.project.pattern.OfbizGroovyPatterns.ENTITY_CALL
import static fr.nereide.project.pattern.OfbizGroovyPatterns.GENERIC_VALUE_ATTRIBUTE
import static fr.nereide.project.pattern.OfbizGroovyPatterns.SERVICE_CALL

class GroovyCompletionContributor extends OfbizBaseCompletionContributor {
    GroovyCompletionContributor() {
        this.extend(CompletionType.BASIC, ENTITY_CALL, entityOrViewNameCompletionProvider)
        this.extend(CompletionType.BASIC, SERVICE_CALL, serviceNameCompletionProvider)
        this.extend(CompletionType.BASIC, GENERIC_VALUE_ATTRIBUTE, new GroovyEntityFieldCompletionProvider())
    }
}
