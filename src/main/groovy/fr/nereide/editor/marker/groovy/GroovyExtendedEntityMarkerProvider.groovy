package fr.nereide.editor.marker.groovy

import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import fr.nereide.editor.marker.common.CommonEntityExtendMarkerProvider
import fr.nereide.project.pattern.OfbizGroovyPatterns

/**
 * Groovy specific eca marker class
 */
class GroovyExtendedEntityMarkerProvider extends CommonEntityExtendMarkerProvider {

    final PsiElementPattern pattern = OfbizGroovyPatterns.ENTITY_CALL
    final Class leafElementType = PsiElement

}
