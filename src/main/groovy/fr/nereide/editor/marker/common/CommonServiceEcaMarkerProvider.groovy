package fr.nereide.editor.marker.common

import com.intellij.psi.PsiElement
import fr.nereide.editor.marker.OfbizBaseLineMarker
import fr.nereide.editor.renderer.ServiceEcaPresentationRenderer
import fr.nereide.project.OfbizProjectHelper
import icons.PluginIcons

import javax.swing.Icon
import java.util.function.Supplier

abstract class CommonServiceEcaMarkerProvider extends OfbizBaseLineMarker {

    ServiceEcaPresentationRenderer getRenderer() {
        return new ServiceEcaPresentationRenderer()
    }

    Icon getIcon() {
        return PluginIcons.ECA_ICON
    }

    String getListTitle() {
        return 'ECA list'
    }

    Closure<String> getTooltipProvider(List<PsiElement> navEls) {
        return (psiElement) -> "${navEls.size()} ECA(s) present on service" as String
    }

    Supplier<String> getMessageSupplier() {
        return { 'Service ECA detected' }
    }

    List<PsiElement> getNavigatableList(PsiElement element) {
        return OfbizProjectHelper.getInstance(element.project)
                .getEcasForService(element)
                .collect { it.getXmlElement().getNavigationElement() }
    }
}
