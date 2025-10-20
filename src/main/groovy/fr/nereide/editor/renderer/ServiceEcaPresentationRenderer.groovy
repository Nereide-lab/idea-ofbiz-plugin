package fr.nereide.editor.renderer

import com.intellij.codeInsight.navigation.impl.PsiTargetPresentationRenderer
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.platform.backend.presentation.TargetPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlTag
import com.intellij.util.xml.DomManager
import fr.nereide.dom.element.serviceeca.Eca
import fr.nereide.project.utils.MiscUtils
import icons.PluginIcons
import org.jetbrains.annotations.NotNull

import javax.swing.Icon
import java.awt.Color

/**
 * Class responsible for rendering the text that appears when hovering over a gutter icon for ECA
 */
class ServiceEcaPresentationRenderer extends PsiTargetPresentationRenderer<PsiElement> {

    String getElementText(PsiElement element) {
        return "ECA in [Component : ${MiscUtils.getComponentName(element)}]"
    }

    @Override
    TargetPresentation getPresentation(@NotNull PsiElement element) {
        // noinspection UnstableApiUsage
        return new TargetPresentation() {

            final String presentableText = 'ECA'
            final String locationText = "[Component : ${MiscUtils.getComponentName(element)}]"
            final Icon icon = PluginIcons.ECA_ICON
            final TextAttributes presentableTextAttributes = null
            final TextAttributes containerTextAttributes = null
            final Icon locationIcon = null
            final Color backgroundColor = null

            String getContainerText() {
                Eca eca = DomManager.getDomManager(element.project).getDomElement(element as XmlTag) as Eca
                StringBuilder sb = new StringBuilder()
                sb.append('event: ')
                sb.append(eca.event?.value ?: 'NONE')
                sb.append(' | ')
                sb.append('service: ')
                sb.append(eca.actions.collect { action -> action.service.value }.join(', '))
                return sb
            }

        }
    }

}
