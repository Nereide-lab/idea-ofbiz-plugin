package fr.nereide.editor.marker.common

import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import fr.nereide.editor.marker.OfbizBaseLineMarker
import fr.nereide.editor.renderer.ExtendEntityPresentationRenderer
import fr.nereide.project.OfbizProjectHelper
import icons.PluginIcons

import javax.swing.Icon
import java.util.function.Supplier

/**
 * Class that marks in gutter entities that are extended
 */
class CommonEntityExtendMarkerProvider extends OfbizBaseLineMarker {

    final PsiElementPattern pattern = null
    final Icon icon = PluginIcons.ENTITY_ICON
    final Class leafElementType = null
    final String listTitle = 'Extends list'
    final Supplier<String> messageSupplier = { 'Entity is extended' }

    ExtendEntityPresentationRenderer getRenderer() {
        return new ExtendEntityPresentationRenderer()
    }

    Closure<String> getTooltipProvider(List<PsiElement> navEls) { // codenarc-disable UnusedMethodParameter
        return (psiElement) -> 'Entity is extended'
    }

    List<PsiElement> getNavigatableList(PsiElement element) {
        return OfbizProjectHelper.getInstance(element.project)
                .getExtendEntityListForEntity(element)
                .collect { extend -> extend.xmlElement.navigationElement }
    }

}
