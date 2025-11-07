package fr.nereide.project.utils

import static fr.nereide.project.pattern.OfbizPluginConstants.CLOSING_BRACKET
import static fr.nereide.project.pattern.OfbizPluginConstants.DOT_MAP_PROP_ACCESSOR
import static fr.nereide.project.pattern.OfbizPluginConstants.DYNAMIC_STRING_DOLLAR

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

/**
 * Small utility class for readability
 */
class UiLabelTextRange extends TextRange {

    UiLabelTextRange(PsiElement element) {
        super(element.text.indexOf(DOT_MAP_PROP_ACCESSOR) + 1, element.text.indexOf(CLOSING_BRACKET))
    }

    UiLabelTextRange(PsiElement element, int index) {
        super(getIndexOf(element, index, DOT_MAP_PROP_ACCESSOR) + 1, getIndexOf(element, index, CLOSING_BRACKET))
    }

    static int getIndexOf(PsiElement element, int index, String str) {
        String fullText = element.text
        String[] parts = fullText.split(DYNAMIC_STRING_DOLLAR).collect(part -> part.trim() ?: null)
        String relevantPart = parts[index]
        return fullText.indexOf(
                str,
                fullText.indexOf(relevantPart),
                fullText.split(DYNAMIC_STRING_DOLLAR).take(index + 1).join('').size() + index
        )
    }

}
