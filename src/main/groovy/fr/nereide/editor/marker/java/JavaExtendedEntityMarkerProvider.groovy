package fr.nereide.editor.marker.java

import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import fr.nereide.editor.marker.common.CommonEntityExtendMarkerProvider
import fr.nereide.project.pattern.OfbizJavaPatterns

/**
 * Groovy specific eca marker class
 */
class JavaExtendedEntityMarkerProvider extends CommonEntityExtendMarkerProvider {

    final PsiElementPattern pattern = OfbizJavaPatterns.ENTITY_CALL
    final Class leafElementType = PsiElement

}
