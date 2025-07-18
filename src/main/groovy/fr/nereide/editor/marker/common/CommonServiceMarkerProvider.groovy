package fr.nereide.editor.marker.common

import com.intellij.psi.PsiElement
import fr.nereide.editor.marker.OfbizBaseLineMarker
import fr.nereide.editor.renderer.ServiceEcaPresentationRenderer
import fr.nereide.project.OfbizProjectHelper

import java.util.function.Supplier

abstract class CommonServiceMarkerProvider extends OfbizBaseLineMarker {

    ServiceEcaPresentationRenderer getRenderer() {
        return new ServiceEcaPresentationRenderer()
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
