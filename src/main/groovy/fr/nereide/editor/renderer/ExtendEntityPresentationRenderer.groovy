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
