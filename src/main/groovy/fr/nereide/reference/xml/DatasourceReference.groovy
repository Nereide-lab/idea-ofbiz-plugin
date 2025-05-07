package fr.nereide.reference.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.util.xml.DomElement
import fr.nereide.project.OfbizProjectHelper
import org.jetbrains.annotations.Nullable


class DatasourceReference extends PsiReferenceBase<PsiElement> {
    DatasourceReference(PsiElement element) {
        super(element)
    }

    @Nullable
    PsiElement resolve() {
        DomElement datasource = OfbizProjectHelper.getInstance(this.element.project).getDatasource(this.getValue())
        return datasource ? datasource.getXmlElement() : null
    }

}
