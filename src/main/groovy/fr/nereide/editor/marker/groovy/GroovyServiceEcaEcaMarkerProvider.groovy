package fr.nereide.editor.marker.groovy

import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import fr.nereide.editor.marker.common.CommonServiceEcaMarkerProvider
import fr.nereide.project.pattern.OfbizGroovyPatterns

class GroovyServiceEcaEcaMarkerProvider extends CommonServiceEcaMarkerProvider {

    PsiElementPattern getPattern() { return OfbizGroovyPatterns.SERVICE_CALL }

    Class getLeafElementType() { return PsiElement.class }
}
