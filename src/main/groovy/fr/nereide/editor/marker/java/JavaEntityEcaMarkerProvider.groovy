package fr.nereide.editor.marker.java

import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiJavaToken
import fr.nereide.editor.marker.common.CommonEntityEcaMarkerProvider
import fr.nereide.project.pattern.OfbizJavaPatterns

/**
 * Sub class for ECA Marking
 */
class JavaEntityEcaMarkerProvider extends CommonEntityEcaMarkerProvider {

    final PsiElementPattern pattern = OfbizJavaPatterns.ENTITY_CALL
    final Class leafElementType = PsiJavaToken

}
