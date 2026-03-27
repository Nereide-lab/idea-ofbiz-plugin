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

import static fr.nereide.project.pattern.OfbizPluginConstants.DYNAMIC_STRING_DOLLAR
import static fr.nereide.project.utils.MiscUtils.getUiLabelSafeValue

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttribute
import fr.nereide.inspection.InspectionBundle
import fr.nereide.inspection.common.OfbizBaseInspection
import fr.nereide.inspection.quickfix.xml.CreateMissingLabelFix
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.pattern.OfbizXmlPatterns
import fr.nereide.project.utils.UiLabelTextRange
import org.jetbrains.annotations.NotNull

/**
 * Checks labels exists
 */
class LabelNotFoundInXmlInspection extends OfbizBaseInspection {

    CreateMissingLabelFix myQuickFix = new CreateMissingLabelFix()

    /* codenarc-disable FactoryMethodName, UnusedMethodParameter */

    PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        /* codenarc-enable FactoryMethodName UnusedMethodParameter */
        return new XmlElementVisitor() {

            void visitXmlAttribute(@NotNull XmlAttribute attribute) {
                if (PluginActivator.getInstance(attribute.project).inactive) return
                if (!OfbizXmlPatterns.LABEL_CALL.accepts(attribute.valueElement)) return
                OfbizProjectHelper ph = OfbizProjectHelper.getInstance(attribute.project)
                String[] labels = attribute.value.split(DYNAMIC_STRING_DOLLAR)*.trim()
                for (int i = 0; i < labels.size(); i++) {
                    if (labels[i].trim().empty || ph.getProperty(getUiLabelSafeValue(labels[i]))) {
                        continue
                    }
                    String foo = labels[i]
                    String foo1 = getUiLabelSafeValue(labels[i])
                    holder.registerProblem(
                            attribute,
                            InspectionBundle.message('inspection.label.not.found.display.descriptor'),
                            ProblemHighlightType.WARNING,
                            new UiLabelTextRange(attribute, i),
                            myQuickFix)
                }
            }

        }
    }

}
