/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.ofbiz.idea.plugin.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttribute
import org.apache.ofbiz.idea.plugin.inspection.quickfix.AdjustFileLocationPathFix
import org.apache.ofbiz.idea.plugin.project.ProjectServiceInterface
import org.apache.ofbiz.idea.plugin.project.utils.FileHandlingUtils
import org.jetbrains.annotations.NotNull

class EmptyFileLocationInspection extends LocalInspectionTool {

    private final AdjustFileLocationPathFix myQuickFix = new AdjustFileLocationPathFix()

    @Override
    boolean isEnabledByDefault() {
        return true
    }

    @Override
    @NotNull
    PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new XmlElementVisitor() {
            @Override
            void visitXmlAttribute(@NotNull XmlAttribute attribute) {
                if (attribute.getName() != 'location') {
                    return
                }
                String attrValue = attribute.getValue()
                if (!attrValue.startsWith('component://')) {
                    return
                }

                //Actual control
                ProjectServiceInterface ps = attribute.getOriginalElement().getProject().getService(ProjectServiceInterface.class)
                if (!FileHandlingUtils.getTargetFile(attrValue, ps)) {
                    holder.registerProblem(
                            attribute,
                            InspectionBundle.message('inspection.location.target.file.not.found.display.descriptor'),
                            ProblemHighlightType.WARNING,
                            myQuickFix
                    )
                }
            }
        }
    }
}
