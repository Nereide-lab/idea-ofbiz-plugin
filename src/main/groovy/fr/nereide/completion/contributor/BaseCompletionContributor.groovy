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
package fr.nereide.completion.contributor

import com.intellij.codeInsight.completion.CompletionContributor
import fr.nereide.completion.provider.common.EntityOrViewNameCompletionProvider
import fr.nereide.completion.provider.common.ServiceNameCompletionProvider

/**
 * Part of the OFBiz plugin Completion system
 */
class BaseCompletionContributor extends CompletionContributor {

    EntityOrViewNameCompletionProvider entityOrViewNameCompletionProvider
    ServiceNameCompletionProvider serviceNameCompletionProvider

    BaseCompletionContributor() {
        entityOrViewNameCompletionProvider = new EntityOrViewNameCompletionProvider()
        serviceNameCompletionProvider = new ServiceNameCompletionProvider()
    }

}
