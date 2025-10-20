package fr.nereide.reference.xml.provider

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext
import fr.nereide.project.PluginActivator
import fr.nereide.reference.xml.EngineReference
import org.jetbrains.annotations.NotNull

/**
 * Part of the OFBiz plugin reference and navigation system
 */
class ServiceEngineReferenceProvider extends PsiReferenceProvider {

    /* codenarc-disable UnusedMethodParameter */

    @NotNull
    PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        /* codenarc-enable UnusedMethodParameter */
        if (PluginActivator.getInstance(element.project).inactive) return []
        EngineReference ref = new EngineReference(element)
        return ref ? ref as PsiReference[] : PsiReference.EMPTY_ARRAY
    }

}
