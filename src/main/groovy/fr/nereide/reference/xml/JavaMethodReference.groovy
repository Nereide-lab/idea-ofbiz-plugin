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
package fr.nereide.reference.xml

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import org.jetbrains.annotations.Nullable

/**
 * Part of the OFBiz plugin reference and navigation system
 */
class JavaMethodReference extends PsiReferenceBase<XmlElement> {

    private final PsiClass currentClass

    JavaMethodReference(XmlAttributeValue methodName, PsiClass currentClass, boolean soft) {
        super(methodName, soft)
        this.currentClass = currentClass
    }

    @Nullable
    PsiElement resolve() {
        return this.currentClass?.methods?.find { method -> method.name == this.value }
    }

}
