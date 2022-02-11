package fr.nereide.project.pattern

import com.intellij.patterns.PatternCondition
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiType
import com.intellij.psi.PsiVariable
import com.intellij.psi.util.TypeConversionUtil
import com.intellij.util.ProcessingContext
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElement

class FieldTypeCondition extends PatternCondition<PsiElement> {
    String expectedType

    FieldTypeCondition(@Nullable String debugMethodName, String expectedType) {
        super(debugMethodName)
        this.expectedType = expectedType
    }

    @Override
    boolean accepts(@NotNull PsiElement psiElement, ProcessingContext context) {
        boolean isMatch = false
        GroovyPsiElement
        if (psiElement instanceof PsiVariable) {
            PsiType elType = TypeConversionUtil.erasure((psiElement as PsiVariable).getType())
            isMatch = elType.getCanonicalText() == expectedType
        }
        return isMatch
    }
}