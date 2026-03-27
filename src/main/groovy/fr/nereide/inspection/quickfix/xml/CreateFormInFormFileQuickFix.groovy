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
package fr.nereide.inspection.quickfix.xml

import com.intellij.psi.xml.XmlFile
import fr.nereide.inspection.InspectionBundle

/**
 * Quick-fix that attempts to create a file after a user dialog
 */
class CreateFormInFormFileQuickFix extends BaseOfbizElementCreateQuickFix {

    final String name = InspectionBundle.message('inspection.form.not.found.on.target.use.quickfix.create')

    final String tagType = 'form'

    CreateFormInFormFileQuickFix(XmlFile file, String formName) {
        super(file, formName)
    }

}
