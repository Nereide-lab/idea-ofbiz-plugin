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

class ExtendEntityPresentationRenderer extends PsiTargetPresentationRenderer<PsiElement> {

    String getElementText(PsiElement element) {
        return "Entity extended in ${MiscUtils.getComponentName(element)}]"
    }

    @Override
    TargetPresentation getPresentation(@NotNull PsiElement element) {

        //noinspection UnstableApiUsage
        return new TargetPresentation() {

            String getPresentableText() { return '' }

            String getContainerText() { return '' }

            String getLocationText() { return "[Component : ${MiscUtils.getComponentName(element)}]" }

            Icon getIcon() { return PluginIcons.ECA_ICON }

            TextAttributes getPresentableTextAttributes() { return null }

            TextAttributes getContainerTextAttributes() { return null }

            Icon getLocationIcon() { return null }

            Color getBackgroundColor() { return null }
        }
    }
}
