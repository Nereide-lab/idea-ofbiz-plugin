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
package fr.nereide.inspection.groovy

import static com.intellij.codeInspection.ProblemHighlightType.WARNING
import static fr.nereide.inspection.InspectionBundle.message

import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.pattern.OfbizGroovyPatterns
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.groovy.codeInspection.BaseInspection
import org.jetbrains.plugins.groovy.codeInspection.BaseInspectionVisitor
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral

/**
 * Inspection for entities or views not found
 */
class EntityNotFoundInGroovyInspection extends BaseInspection {

    @Override
    boolean isEnabledByDefault() {
        return true
    }

    @Override
    protected BaseInspectionVisitor buildVisitor() {
        return new BaseInspectionVisitor() {

            @Override
            void visitLiteralExpression(@NotNull GrLiteral element) {
                if (PluginActivator.getInstance(element.project).inactive ||
                        !(OfbizGroovyPatterns.ENTITY_CALL.accepts(element))) return
                OfbizProjectHelper ph = OfbizProjectHelper.getInstance(element.project)
                if (!(ph.entityOrViewExists(element.value))) {
                    this.registerError(
                            element,
                            message('inspection.entity.not.found.display.descriptor'),
                            null,
                            WARNING)
                }
            }

        }
    }

}
