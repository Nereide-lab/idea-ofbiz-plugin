package fr.nereide.reference.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.util.xml.DomElement
import fr.nereide.project.OfbizProjectHelper
import org.jetbrains.annotations.Nullable

class EngineReference extends PsiReferenceBase<PsiElement> {
    EngineReference(PsiElement element) {
        super(element)
    }

    @Nullable
    PsiElement resolve() {
        DomElement serviceEngine = OfbizProjectHelper.getInstance(this.element.project).getEngine(this.getValue())
        return serviceEngine ? serviceEngine.getXmlElement() : null
    }

}
