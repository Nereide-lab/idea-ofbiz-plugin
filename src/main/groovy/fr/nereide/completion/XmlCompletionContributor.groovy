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

package fr.nereide.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import fr.nereide.completion.provider.common.EntityNameCompletionProvider
import fr.nereide.completion.provider.common.ServiceNameCompletionProvider
import fr.nereide.project.OfbizPatterns

class XmlCompletionContributor extends CompletionContributor{
    XmlCompletionContributor(){
        this.extend(CompletionType.BASIC, OfbizPatterns.XML.ENTITY_CALL_COMPL, new EntityNameCompletionProvider())
        this.extend(CompletionType.BASIC, OfbizPatterns.XML.SERVICE_DEF_CALL_COMPL, new ServiceNameCompletionProvider())
    }
}
