package fr.nereide.editor.marker.groovy

import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import fr.nereide.editor.marker.common.CommonEntityEcaMarkerProvider
import fr.nereide.project.pattern.OfbizGroovyPatterns

/**
 * Sub class for ECA Marking
 */
class GroovyEntityEcaMarkerProvider extends CommonEntityEcaMarkerProvider {

    final PsiElementPattern pattern = OfbizGroovyPatterns.ENTITY_CALL

    final Class leafElementType = PsiElement

}
