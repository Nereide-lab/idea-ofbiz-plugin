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
import fr.nereide.completion.provider.java.JavaEntityFieldsFromDVECompletionProvider
import fr.nereide.completion.provider.java.JavaEntityFieldsFromDVEKeyMapCompletionProvider
import fr.nereide.completion.provider.java.JavaEntityFieldsFromGvMethodCompletionProvider
import fr.nereide.completion.provider.java.JavaEntityFieldsInQueryCompletionProvider

import static fr.nereide.project.pattern.OfbizJavaPatterns.ENTITY_CALL_COMPL
import static fr.nereide.project.pattern.OfbizJavaPatterns.ENTITY_FIELD_INKEYMAP_IN_DVE_0
import static fr.nereide.project.pattern.OfbizJavaPatterns.ENTITY_FIELD_INKEYMAP_IN_DVE_1
import static fr.nereide.project.pattern.OfbizJavaPatterns.GENERIC_VALUE_FIELD_FROM_GV_OBJECT
import static fr.nereide.project.pattern.OfbizJavaPatterns.GENERIC_VALUE_FIELD_IN_DVE
import static fr.nereide.project.pattern.OfbizJavaPatterns.GENERIC_VALUE_FIELD_IN_WHERE_QUERY
import static fr.nereide.project.pattern.OfbizJavaPatterns.SERVICE_CALL_COMPL

class JavaCompletionContributor extends OfbizBaseCompletionContributor {

    JavaCompletionContributor() {

        this.extend(CompletionType.BASIC, ENTITY_CALL_COMPL, entityOrViewNameCompletionProvider)
        this.extend(CompletionType.BASIC, SERVICE_CALL_COMPL, serviceNameCompletionProvider)
        this.extend(CompletionType.BASIC, GENERIC_VALUE_FIELD_IN_WHERE_QUERY, new JavaEntityFieldsInQueryCompletionProvider())
        this.extend(CompletionType.BASIC, GENERIC_VALUE_FIELD_FROM_GV_OBJECT, new JavaEntityFieldsFromGvMethodCompletionProvider())
        this.extend(CompletionType.BASIC, GENERIC_VALUE_FIELD_IN_DVE, new JavaEntityFieldsFromDVECompletionProvider())
        this.extend(CompletionType.BASIC, ENTITY_FIELD_INKEYMAP_IN_DVE_0, new JavaEntityFieldsFromDVEKeyMapCompletionProvider(0))
        this.extend(CompletionType.BASIC, ENTITY_FIELD_INKEYMAP_IN_DVE_1, new JavaEntityFieldsFromDVEKeyMapCompletionProvider(1))
    }
}
