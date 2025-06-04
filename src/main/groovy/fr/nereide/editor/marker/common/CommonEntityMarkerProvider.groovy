package fr.nereide.editor.marker.common

import com.intellij.psi.PsiElement
import fr.nereide.editor.marker.OfbizBaseLineMarker
import fr.nereide.editor.renderer.EntityEcaPresentationRenderer
import fr.nereide.project.OfbizProjectHelper

import java.util.function.Supplier

class CommonEntityMarkerProvider extends OfbizBaseLineMarker {

    EntityEcaPresentationRenderer getRenderer() {
        return new EntityEcaPresentationRenderer()
    }

    String getListTitle() {
        return 'ECA list'
    }

    def getTooltipProvider(List<PsiElement> navEls) {
        return ((psiElement) -> "${navEls.size()} ECA(s) present on entity" as String)
    }

    Supplier<String> getMessageSupplier() {
        return { 'Entity ECA detected' }
    }

    List<PsiElement> getNavigatableList(PsiElement element) {
        return OfbizProjectHelper.getInstance(element.project)
                .getEcasForEntity(element)
                .collect { it.getXmlElement().getNavigationElement() }
    }
}
