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
package fr.nereide.editor.renderer

import com.intellij.codeInsight.navigation.impl.PsiTargetPresentationRenderer
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.platform.backend.presentation.TargetPresentation
import com.intellij.psi.PsiElement
import fr.nereide.project.utils.MiscUtils
import icons.PluginIcons
import org.jetbrains.annotations.NotNull

import javax.swing.Icon
import java.awt.Color

/**
 * Class responsible for rendering the text that appears when hovering over a gutter icon for ECA
 */
class ExtendEntityPresentationRenderer extends PsiTargetPresentationRenderer<PsiElement> {

    String getElementText(PsiElement element) {
        return "Entity extended in ${MiscUtils.getComponentName(element)}]"
    }

    @Override
    TargetPresentation getPresentation(@NotNull PsiElement element) {
        // noinspection UnstableApiUsage
        return new TargetPresentation() {

            final String presentableText = ''
            final String containerText = ''
            final String locationText = "[Component : ${MiscUtils.getComponentName(element)}]"
            final Icon icon = PluginIcons.ECA_ICON
            final TextAttributes presentableTextAttributes = null
            final TextAttributes containerTextAttributes = null
            final Icon locationIcon = null
            final Color backgroundColor = null

        }
    }

}
