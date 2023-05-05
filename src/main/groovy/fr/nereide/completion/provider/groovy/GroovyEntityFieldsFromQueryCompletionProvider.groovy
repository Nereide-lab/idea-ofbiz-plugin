package fr.nereide.completion.provider.groovy

import com.intellij.psi.PsiElement
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType

class GroovyEntityFieldsFromQueryCompletionProvider extends GroovyEntityFieldCompletionProvider {

    @Override
    String getEntityNameFromPsiElement(PsiElement psiElement) {
        GrMethodCall query = getParentOfType(psiElement, GrMethodCall.class)
        return query ? getEntityNameFromDeclarationString(query.text) : null
    }
}
