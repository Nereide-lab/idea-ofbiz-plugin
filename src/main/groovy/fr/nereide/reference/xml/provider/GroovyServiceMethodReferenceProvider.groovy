package fr.nereide.reference.xml.provider

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiReference
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.ProcessingContext
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.utils.FileHandlingUtils
import fr.nereide.reference.xml.GroovyServiceDefReference
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.groovy.lang.psi.GroovyFile

class GroovyServiceMethodReferenceProvider extends JavaMethodReferenceProvider {

    @Override
    PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        ProjectServiceInterface ps = element.getProject().getService(ProjectServiceInterface.class)
        String locationAttr = getClassLocation(element)
        if (!locationAttr) return PsiReference.EMPTY_ARRAY
        PsiFile targetedFile = FileHandlingUtils.getTargetFile(locationAttr, ps)
        if (!targetedFile || !(targetedFile instanceof GroovyFile)) return PsiReference.EMPTY_ARRAY
        return new GroovyServiceDefReference(element as XmlAttributeValue,
                (targetedFile as GroovyFile).getScriptClass(),
                true)
    }
}
