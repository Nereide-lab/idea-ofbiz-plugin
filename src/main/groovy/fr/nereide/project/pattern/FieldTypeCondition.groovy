package fr.nereide.project.pattern

import com.intellij.patterns.PatternCondition
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiType
import com.intellij.util.ProcessingContext
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression

class FieldTypeCondition extends PatternCondition<PsiElement> {
    String[] expectedType

    FieldTypeCondition(@Nullable String debugMethodName, String[] expectedType) {
        super(debugMethodName)
        this.expectedType = expectedType
    }

    @Override
    boolean accepts(@NotNull PsiElement element, ProcessingContext context) {
        boolean isMatch = false
        if (element instanceof GrReferenceExpression) {
            PsiType myType = (element as GrReferenceExpression).getType()
            isMatch = myType && expectedType.contains(myType.getCanonicalText())
        }
        return isMatch
    }
}