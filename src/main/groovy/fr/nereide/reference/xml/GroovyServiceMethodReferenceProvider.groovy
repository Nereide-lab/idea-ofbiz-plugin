package fr.nereide.reference.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiReference
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.ProcessingContext
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.utils.FileHandlingUtils
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.groovy.lang.psi.GroovyFile

class GroovyServiceMethodReferenceProvider extends JavaMethodReferenceProvider {

    @Override
    PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        ProjectServiceInterface ps = element.getProject().getService(ProjectServiceInterface.class)
        String locationAttr = getClassLocation(element)
        if (!locationAttr) return null
        PsiFile targetedFile = FileHandlingUtils.getTargetFile(locationAttr, ps)
        if (!targetedFile || !(targetedFile instanceof GroovyFile)) return null
        return new GroovyServiceDefReference(element as XmlAttributeValue,
                (targetedFile as GroovyFile).getScriptClass(),
                true)
    }
}
