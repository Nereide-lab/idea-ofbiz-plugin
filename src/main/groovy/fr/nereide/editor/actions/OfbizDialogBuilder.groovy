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
package fr.nereide.editor.actions

import com.intellij.openapi.project.Project
import fr.nereide.editor.OfbizEditorBundle

/**
 * Base Dialog builder for ofbiz plugin
 */
class OfbizDialogBuilder {

    Project myProject
    Map<String, Object> myChoices
    String myDialTitle
    String myDialText

    OfbizDialogBuilder(Project project) {
        myProject = project
        myChoices = [:]
    }

    /* codenarc-disable SpaceAroundMapEntryColon */

    OfbizDialogBuilder from(List<String> choicesList) {
        choicesList.forEach { option -> myChoices << [(option): null] }
        return this
    }
    /* codenarc-enable SpaceAroundMapEntryColon */

    OfbizDialogBuilder from(Map<String, Object> choices) {
        myChoices = choices
        return this
    }

    OfbizDialogBuilder title(String title) {
        myDialTitle = title
        return this
    }

    OfbizDialogBuilder text(String text) {
        myDialText = text
        return this
    }

    OfbizSimpleListDialog get() {
        return new OfbizSimpleListDialog(myProject, myChoices,
                myDialTitle ?: OfbizEditorBundle.message('editor.choice.title.default'),
                myDialText ?: OfbizEditorBundle.message('editor.choice.select.default')
        )
    }

}
