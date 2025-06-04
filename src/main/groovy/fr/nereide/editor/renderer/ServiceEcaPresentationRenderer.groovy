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

import javax.swing.*
import java.awt.*

class ServiceEcaPresentationRenderer extends PsiTargetPresentationRenderer<PsiElement> {

    String getElementText(PsiElement element) {
        return "ECA in [Component : ${MiscUtils.getComponentName(element)}]"
    }

    @Override
    TargetPresentation getPresentation(@NotNull PsiElement element) {

        //noinspection UnstableApiUsage
        return new TargetPresentation() {

            String getPresentableText() {
                return 'ECA'
            }

            String getContainerText() {
                Eca eca = DomManager.getDomManager(element.project).getDomElement(element as XmlTag) as Eca
                StringBuilder sb = new StringBuilder()
                sb.append('event: ')
                sb.append(eca.event?.value ?: 'NONE')
                sb.append(' | ')
                sb.append('service: ')
                sb.append(eca.actions.collect { it.service.value }.join(', '))
            }

            String getLocationText() { return "[Component : ${MiscUtils.getComponentName(element)}]" }

            Icon getIcon() { return PluginIcons.ECA_ICON }

            TextAttributes getPresentableTextAttributes() { return null }

            TextAttributes getContainerTextAttributes() { return null }

            Icon getLocationIcon() { return null }

            Color getBackgroundColor() { return null }
        }
    }
}
