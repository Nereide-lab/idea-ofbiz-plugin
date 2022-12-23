package fr.nereide.reference.xml.provider

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext
import fr.nereide.reference.xml.DatasourceReference
import org.jetbrains.annotations.NotNull

class DatasourceReferenceProvider extends PsiReferenceProvider {

    PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        DatasourceReference ref = new DatasourceReference(element)
        return ref ? ref as PsiReference[] : PsiReference.EMPTY_ARRAY
    }
}
