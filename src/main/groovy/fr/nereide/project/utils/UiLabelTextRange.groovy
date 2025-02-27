package fr.nereide.project.utils

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

/**
 * Small utility class for readability
 */
class UiLabelTextRange extends TextRange {
    UiLabelTextRange(PsiElement element) {
        super(element.text.indexOf('.') + 1, element.text.indexOf('}'))
    }
}
