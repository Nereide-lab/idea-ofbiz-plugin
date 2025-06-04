package fr.nereide.editor.marker

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationUtil
import com.intellij.codeInsight.navigation.impl.PsiTargetPresentationRenderer
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.patterns.PsiElementPattern
import com.intellij.platform.backend.presentation.TargetPresentation
import com.intellij.pom.Navigatable
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlTag
import com.intellij.ui.awt.RelativePoint
import com.intellij.util.xml.DomManager
import fr.nereide.dom.element.eca.Eca
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.PluginActivator
import fr.nereide.project.utils.MiscUtils
import icons.PluginIcons
import org.jetbrains.annotations.NotNull

import javax.swing.Icon
import java.awt.Color
import java.awt.event.MouseEvent

class BaseServiceEcaMarker implements LineMarkerProvider {

    PsiElementPattern getPattern() {
        return null
    }

    Class getLeafElementType() {
        return null
    }

    @Override
    LineMarkerInfo<PsiElement> getLineMarkerInfo(@NotNull PsiElement element) {
        if (!PluginActivator.getInstance(element.project).isActive()) return null
        if (!getPattern().accepts(element)) return null
        PsiElement elementToRegister = element.getFirstChild()
        if (!getLeafElementType().isAssignableFrom(elementToRegister.getClass())) return

        List<PsiElement> ecaNavs = OfbizProjectHelper.getInstance(element.project)
                .getEcasForService(element)
                .collect { it.getXmlElement().getNavigationElement() }
        if (ecaNavs.size() < 1) return
        return new LineMarkerInfo<PsiElement>(
                elementToRegister,
                element.getTextRange(),
                PluginIcons.ECA_ICON,
                ((psiElement) -> "${ecaNavs.size()} ECA(s) present on service" as String),
                getNavHandler(ecaNavs),
                GutterIconRenderer.Alignment.RIGHT,
                { 'Service ECA detected' }
        )
    }

    private GutterIconNavigationHandler<PsiElement> getNavHandler(List<PsiElement> ecaNavs) {
        return new GutterIconNavigationHandler<PsiElement>() {
            @Override
            void navigate(MouseEvent e, PsiElement elt) {
                if (ecaNavs.size() == 1) {
                    Navigatable onlyEca = ecaNavs.first as Navigatable
                    if (onlyEca.canNavigate()) onlyEca.navigate(true)
                } else {
                    NavigationUtil.getPsiElementPopup(
                            { -> ecaNavs },
                            new EcaRenderer(),
                            'ECA List',
                            elt.project
                    ).show(new RelativePoint(e))
                }
            }
        }
    }

    class EcaRenderer extends PsiTargetPresentationRenderer<PsiElement> {

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
}
