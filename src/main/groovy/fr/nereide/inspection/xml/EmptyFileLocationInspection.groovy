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
package fr.nereide.inspection.xml

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttribute
import fr.nereide.inspection.InspectionBundle
import fr.nereide.inspection.common.OfbizBaseInspection
import fr.nereide.inspection.quickfix.xml.AdjustFileLocationPathFix
import fr.nereide.inspection.quickfix.xml.CreateFileAtLocationFix
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.utils.FileHandlingUtils
import org.jetbrains.annotations.NotNull

/**
 * Inspection that checks files references to a non existing file
 */
class EmptyFileLocationInspection extends OfbizBaseInspection {

    private final AdjustFileLocationPathFix myChangePathQuickFix = new AdjustFileLocationPathFix()
    private final CreateFileAtLocationFix myCreateFileQuickFix = new CreateFileAtLocationFix()

    @Override
    @NotNull
    PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new XmlElementVisitor() {

            @Override
            void visitXmlAttribute(@NotNull XmlAttribute attribute) {
                if (PluginActivator.getInstance(attribute.project).inactive) return
                if (!attribute.name || !(['path', 'location'].contains(attribute.name))) {
                    return
                }
                String attrValue = attribute.value
                if (!attrValue || (!attrValue.startsWith('component://')) || (attrValue.contains('${'))) {
                    return
                }

                // Actual control
                OfbizProjectHelper ph = OfbizProjectHelper.getInstance(attribute.originalElement.project)
                if (!ph.getPsiFileAtLocation(attrValue)) {
                    holder.registerProblem(
                            attribute,
                            InspectionBundle.message('inspection.location.target.file.not.found.display.descriptor'),
                            ProblemHighlightType.WARNING,
                            myChangePathQuickFix,
                            doesComponentExists(ph, attrValue) ? myCreateFileQuickFix : null
                    )
                }
            }

        }
    }

    private static PsiDirectory doesComponentExists(OfbizProjectHelper ph, String attrValue) {
        return ph.getComponentDir(FileHandlingUtils.splitPathToList(attrValue)[0])
    }

}
