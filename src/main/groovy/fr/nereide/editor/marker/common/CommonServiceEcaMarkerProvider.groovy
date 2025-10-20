package fr.nereide.editor.marker.common

import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import fr.nereide.editor.marker.OfbizBaseLineMarker
import fr.nereide.editor.renderer.ServiceEcaPresentationRenderer
import fr.nereide.project.OfbizProjectHelper
import icons.PluginIcons

import javax.swing.Icon
import java.util.function.Supplier

/**
 * Class that marks in gutter services that have an ECA
 */
class CommonServiceEcaMarkerProvider extends OfbizBaseLineMarker {

    final Icon icon = PluginIcons.ECA_ICON
    final String listTitle = 'ECA list'
    final Supplier<String> messageSupplier = { 'Service ECA detected' }
    final PsiElementPattern pattern = null
    final Class leafElementType = null

    ServiceEcaPresentationRenderer getRenderer() {
        return new ServiceEcaPresentationRenderer()
    }

    Closure<String> getTooltipProvider(List<PsiElement> navEls) {
        return (psiElement) -> { "${navEls.size()} ECA(s) present on service" as String }
    }

    List<PsiElement> getNavigatableList(PsiElement element) {
        return OfbizProjectHelper.getInstance(element.project)
                .getEcasForService(element)
                .collect { eca -> eca.xmlElement.navigationElement }
    }

}
