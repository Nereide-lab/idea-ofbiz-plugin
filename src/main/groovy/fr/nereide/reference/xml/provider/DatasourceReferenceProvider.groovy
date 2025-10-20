package fr.nereide.reference.xml.provider

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext
import fr.nereide.project.PluginActivator
import fr.nereide.reference.xml.DatasourceReference
import org.jetbrains.annotations.NotNull

/**
 * Part of the OFBiz plugin reference and navigation system
 */
class DatasourceReferenceProvider extends PsiReferenceProvider {

    // codenarc-disable UnusedMethodParameter
    PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        if (PluginActivator.getInstance(element.project).inactive) return []
        DatasourceReference ref = new DatasourceReference(element)
        return ref ? ref as PsiReference[] : PsiReference.EMPTY_ARRAY
    }

}
