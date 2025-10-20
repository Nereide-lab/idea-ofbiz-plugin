package fr.nereide.editor.renderer

import com.intellij.codeInsight.navigation.impl.PsiTargetPresentationRenderer
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.platform.backend.presentation.TargetPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlTag
import com.intellij.util.xml.DomManager
import fr.nereide.dom.element.entityeca.Eca
import fr.nereide.project.utils.MiscUtils
import icons.PluginIcons
import org.jetbrains.annotations.NotNull
import javax.swing.Icon
import java.awt.Color

/**
 * Class responsible for rendering the text that appears when hovering over a gutter icon for ECA
 */
class EntityEcaPresentationRenderer extends PsiTargetPresentationRenderer<PsiElement> {

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
                String none = 'NONE'
                Eca eca = DomManager.getDomManager(element.project).getDomElement(element as XmlTag) as Eca
                StringBuilder sb = new StringBuilder().with {
                    append('event: ')
                    append(eca.event?.value ?: none)
                    append(' | ')
                    append('operation: ')
                    append(eca.operation?.value ?: none)
                    append(' | service: ')
                    append(eca.actions.collect { action -> action.service.value }.join(', '))
                }
                return sb.toString()
            }

        }
    }

}
