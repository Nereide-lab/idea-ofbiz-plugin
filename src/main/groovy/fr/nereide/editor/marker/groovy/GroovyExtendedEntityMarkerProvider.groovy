package fr.nereide.editor.marker.groovy

import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import fr.nereide.editor.marker.common.CommonEntityExtendMarkerProvider
import fr.nereide.project.pattern.OfbizGroovyPatterns

class GroovyExtendedEntityMarkerProvider extends CommonEntityExtendMarkerProvider {

    PsiElementPattern getPattern() { return OfbizGroovyPatterns.ENTITY_CALL }

    Class getLeafElementType() { return PsiElement.class }
}
