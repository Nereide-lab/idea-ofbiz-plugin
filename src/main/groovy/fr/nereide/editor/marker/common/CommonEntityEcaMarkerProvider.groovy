package fr.nereide.editor.marker.common

import com.intellij.codeInsight.navigation.impl.PsiTargetPresentationRenderer
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import fr.nereide.editor.marker.OfbizBaseLineMarker
import fr.nereide.editor.renderer.EntityEcaPresentationRenderer
import fr.nereide.project.OfbizProjectHelper
import icons.PluginIcons

import javax.swing.Icon
import java.util.function.Supplier

/**
 * Class that marks in gutter entities that have an ECA
 */
class CommonEntityEcaMarkerProvider extends OfbizBaseLineMarker {

    final Icon icon = PluginIcons.ECA_ICON
    final String listTitle = 'ECA list'
    final PsiElementPattern pattern = null
    final Class leafElementType = null
    final Supplier<String> messageSupplier = { 'Entity ECA detected' }

    PsiTargetPresentationRenderer<PsiElement> getRenderer() {
        return new EntityEcaPresentationRenderer()
    }

    Closure<String> getTooltipProvider(List<PsiElement> navEls) {
        return (psiElement) -> { "${navEls.size()} ECA(s) present on entity" as String }
    }

    List<PsiElement> getNavigatableList(PsiElement element) {
        return OfbizProjectHelper.getInstance(element.project)
                .getEcasForEntity(element)
                .collect { eca -> eca.xmlElement.navigationElement }
    }

}
