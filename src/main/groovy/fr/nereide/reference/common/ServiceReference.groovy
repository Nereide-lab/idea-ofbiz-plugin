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

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiPolyVariantReferenceBase
import com.intellij.psi.ResolveResult
import fr.nereide.dom.element.service.Service
import fr.nereide.project.OfbizProjectHelper

/**
 * Part of the OFBiz plugin reference and navigation system
 */
class ServiceReference extends PsiPolyVariantReferenceBase<PsiElement> {

    ServiceReference(PsiElement element) {
        super(element)
    }

    @Override
    ResolveResult[] multiResolve(boolean incompleteCode) {
        List<ResolveResult> results = []
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(this.element.project)
        List<Service> definitions = ph.getServices(this.value)
        if (!definitions) return ResolveResult.EMPTY_ARRAY
        for (Service service : definitions) {
            results << new PsiElementResolveResult(service.xmlElement)
        }
        return results.toArray(new ResolveResult[definitions.size()])
    }

    @Override
    boolean isSoft() {
        return true
    }

}
