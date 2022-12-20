package fr.nereide.reference.common

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.util.xml.DomElement
import fr.nereide.project.ProjectServiceInterface
import org.jetbrains.annotations.Nullable

class BaseEntityReference extends PsiReferenceBase {
    BaseEntityReference(PsiElement elementName) {
        super(elementName)
    }

    @Nullable
    PsiElement resolve() {
        ProjectServiceInterface structureService = this.getElement().getProject().getService(ProjectServiceInterface.class)
        DomElement definition = structureService.getEntity(this.getValue())
        if (!definition) definition = structureService.getViewEntity(this.getValue())
        return definition != null ? definition.getXmlElement() : null
    }
}
