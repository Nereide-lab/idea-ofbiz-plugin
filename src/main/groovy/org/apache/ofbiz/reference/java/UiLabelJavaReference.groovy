package org.apache.ofbiz.reference.java

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiReferenceBase
import com.intellij.util.xml.DomElement
import org.apache.ofbiz.project.ProjectServiceInterface
import org.jetbrains.annotations.Nullable

class UiLabelJavaReference extends PsiReferenceBase<PsiLiteralExpression> {
    UiLabelJavaReference(PsiLiteralExpression element, boolean soft) {
        super(element, soft)
    }

    @Nullable
    PsiElement resolve() {
        ProjectServiceInterface structureService = this.getElement().getProject().getService(ProjectServiceInterface.class)
        DomElement definition = structureService.getProperty(this.getUiLabelAwareValue())
        return definition != null ? definition.getXmlElement() : null
    }

    String getUiLabelAwareValue() {
        String text = this.getValue()
        if (text.startsWith('${')) {
            return text.substring(13, text.length() - 1)
        }
        return text;
    }
}
