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
package fr.nereide.editor.marker.common

import com.intellij.codeInsight.navigation.impl.PsiTargetPresentationRenderer
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import fr.nereide.editor.marker.OfbizBaseLineMarker
import fr.nereide.editor.renderer.EntityEcaPresentationRenderer
import fr.nereide.project.OfbizProjectHelper
import icons.PluginIcons

import javax.swing.Icon
import java.util.function.Supplier

/**
 * Class that marks in gutter entities that have an ECA
 */
class CommonEntityEcaMarkerProvider extends OfbizBaseLineMarker {

    final Icon icon = PluginIcons.ECA_ICON
    final String listTitle = 'ECA list'
    final PsiElementPattern pattern = null
    final Class leafElementType = null
    final Supplier<String> messageSupplier = { 'Entity ECA detected' }

    PsiTargetPresentationRenderer<PsiElement> getRenderer() {
        return new EntityEcaPresentationRenderer()
    }

    Closure<String> getTooltipProvider(List<PsiElement> navEls) {
        return (psiElement) -> { "${navEls.size()} ECA(s) present on entity" as String }
    }

    List<PsiElement> getNavigatableList(PsiElement element) {
        return OfbizProjectHelper.getInstance(element.project)
                .getEcasForEntity(element)
                .collect { eca -> eca.xmlElement.navigationElement }
    }

}
