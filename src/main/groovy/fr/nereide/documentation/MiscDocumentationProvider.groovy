package fr.nereide.documentation

import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import fr.nereide.project.OfbizPatterns
import fr.nereide.project.ProjectServiceInterface
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

class MiscDocumentationProvider extends OfbizDocumentationProvider {

    @Override
    PsiElement getCustomDocumentationElement(@NotNull Editor editor, @NotNull PsiFile file, @Nullable PsiElement contextElement, int targetOffset) {
        // Ici, on recoit un XML_ATTRIBUTE_VALUE_TOKEN (apeupré)

        ProjectServiceInterface structureService = contextElement.getProject().getService(ProjectServiceInterface.class)
        PsiElement xmlAttr = PsiTreeUtil.getParentOfType(contextElement, XmlAttribute.class)
        if (xmlAttr) {
            if (OfbizPatterns.XML.SERVICE_DEF_CALL.accepts(xmlAttr)) {
                String elementName = (xmlAttr as XmlAttribute).getValue()
//                return contextElement
//                return structureService.getService(elementName)
            }
        }

        return super.getCustomDocumentationElement(editor, file, contextElement, targetOffset)

    }
}

