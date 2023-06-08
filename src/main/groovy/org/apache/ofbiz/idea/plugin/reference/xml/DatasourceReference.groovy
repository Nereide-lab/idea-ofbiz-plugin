package org.apache.ofbiz.idea.plugin.reference.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.util.xml.DomElement
import org.apache.ofbiz.idea.plugin.project.ProjectServiceInterface
import org.jetbrains.annotations.Nullable


class DatasourceReference extends PsiReferenceBase<PsiElement> {
    DatasourceReference(PsiElement element) {
        super(element)
    }

    @Nullable
    PsiElement resolve() {
        ProjectServiceInterface stSer = this.getElement().getProject().getService(ProjectServiceInterface)
        DomElement datasource = stSer.getDatasource(this.getValue())
        return datasource ? datasource.getXmlElement() : null
    }

}
