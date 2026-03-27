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
package fr.nereide.editor.marker

import static com.intellij.openapi.editor.markup.GutterIconRenderer.Alignment

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationUtil
import com.intellij.codeInsight.navigation.impl.PsiTargetPresentationRenderer
import com.intellij.patterns.PsiElementPattern
import com.intellij.pom.Navigatable
import com.intellij.psi.PsiElement
import com.intellij.ui.awt.RelativePoint
import fr.nereide.project.PluginActivator
import org.jetbrains.annotations.NotNull

import java.awt.event.MouseEvent
import javax.swing.Icon
import java.util.function.Supplier

/**
 * Base interface for ofbiz gutter icons
 */
abstract class OfbizBaseLineMarker implements LineMarkerProvider {

    abstract PsiElementPattern getPattern()

    abstract Class getLeafElementType()

    abstract PsiTargetPresentationRenderer<PsiElement> getRenderer()

    abstract String getListTitle()

    abstract Closure<String> getTooltipProvider(List<PsiElement> navEls)

    abstract Supplier<String> getMessageSupplier()

    abstract List<PsiElement> getNavigatableList(PsiElement element)

    abstract Icon getIcon()

    @Override
    LineMarkerInfo<PsiElement> getLineMarkerInfo(@NotNull PsiElement element) {
        if (PluginActivator.getInstance(element.project).inactive) return null
        if (!pattern.accepts(element)) return null
        PsiElement elementToRegister = element.firstChild
        if (!leafElementType.isAssignableFrom(elementToRegister.getClass())) return null

        List<PsiElement> navEls = getNavigatableList(element)

        if (navEls.size() < 1) return null
        return new LineMarkerInfo<PsiElement>(
                elementToRegister,
                element.textRange,
                icon,
                getTooltipProvider(navEls),
                getNavHandler(navEls),
                Alignment.RIGHT,
                messageSupplier
        )
    }

    private GutterIconNavigationHandler<PsiElement> getNavHandler(List<PsiElement> navigatableList) {
        return new GutterIconNavigationHandler<PsiElement>() {

            @Override
            void navigate(MouseEvent e, PsiElement elt) {
                if (navigatableList.size() == 1) {
                    Navigatable elementAlone = navigatableList.first as Navigatable
                    if (elementAlone.canNavigate()) elementAlone.navigate(true)
                } else {
                    NavigationUtil.getPsiElementPopup(
                            { -> navigatableList },
                            renderer,
                            listTitle,
                            elt.project
                    ).show(new RelativePoint(e))
                }
            }

        }
    }

}
