package fr.nereide.editor.marker.common

import com.intellij.psi.PsiElement
import fr.nereide.editor.marker.OfbizBaseLineMarker
import fr.nereide.editor.renderer.ExtendEntityPresentationRenderer
import fr.nereide.project.OfbizProjectHelper
import icons.PluginIcons

import javax.swing.*
import java.util.function.Supplier

abstract class CommonEntityExtendMarkerProvider extends OfbizBaseLineMarker {

    ExtendEntityPresentationRenderer getRenderer() {
        return new ExtendEntityPresentationRenderer()
    }

    Icon getIcon() {
        return PluginIcons.ENTITY_ICON
    }

    String getListTitle() {
        return 'Extends list'
    }

    Closure<String> getTooltipProvider(List<PsiElement> navEls) {
        return (psiElement) -> 'Entity is extended'
    }

    Supplier<String> getMessageSupplier() {
        return { 'Entity is extended' }
    }

    List<PsiElement> getNavigatableList(PsiElement element) {
        return OfbizProjectHelper.getInstance(element.project)
                .getExtendEntityListForEntity(element)
                .collect { it.getXmlElement().getNavigationElement() }
    }
}
