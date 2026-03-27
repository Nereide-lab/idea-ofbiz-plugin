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
package fr.nereide.reference.common

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.util.xml.DomElement
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.utils.MiscUtils
import org.jetbrains.annotations.Nullable

/**
 * Part of the OFBiz plugin reference and navigation system
 */
class UiLabelReference extends PsiReferenceBase<PsiElement> {

    UiLabelReference(PsiElement element) {
        super(element)
    }

    UiLabelReference(PsiElement element, TextRange range) {
        super(element, range)
    }

    @Nullable
    PsiElement resolve() {
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(this.element.project)
        DomElement definition = ph.getProperty(MiscUtils.getUiLabelSafeValue(this.value))
        return definition != null ? definition.xmlElement : null
    }

    boolean isSoft() {
        return true
    }

}
