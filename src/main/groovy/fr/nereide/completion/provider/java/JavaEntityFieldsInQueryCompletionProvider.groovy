package fr.nereide.completion.provider.java

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethodCallExpression
import fr.nereide.project.pattern.OfbizPatternConst

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType

class JavaEntityFieldsInQueryCompletionProvider extends JavaEntityFieldsCompletionProvider {

    @Override
    String getEntityNameFromPsiElement(PsiElement element) {
        PsiMethodCallExpression originMethod = getParentOfType(element, PsiMethodCallExpression.class)
        if (originMethod && originMethod.text.startsWith(OfbizPatternConst.QUERY_BEGINNING_STRING)) {
            return getEntityNameFromDeclarationString(originMethod.text)
        }
        return null
    }
}
