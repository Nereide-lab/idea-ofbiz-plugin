package fr.nereide.editor.marker.groovy

import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import fr.nereide.editor.marker.common.CommonServiceEcaMarkerProvider
import fr.nereide.project.pattern.OfbizGroovyPatterns

/**
 * Groovy Specific eca marker class
 */
class GroovyServiceEcaEcaMarkerProvider extends CommonServiceEcaMarkerProvider {

    final PsiElementPattern pattern = OfbizGroovyPatterns.SERVICE_CALL
    final Class leafElementType = PsiElement

}
