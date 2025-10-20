package fr.nereide.editor.marker.java

import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiJavaToken
import fr.nereide.editor.marker.common.CommonServiceEcaMarkerProvider
import fr.nereide.project.pattern.OfbizJavaPatterns

/**
 * Groovy Specific eca marker class
 */
class JavaServiceEcaEcaMarkerProvider extends CommonServiceEcaMarkerProvider {

    final PsiElementPattern pattern = OfbizJavaPatterns.SERVICE_CALL
    final Class leafElementType = PsiJavaToken

}
