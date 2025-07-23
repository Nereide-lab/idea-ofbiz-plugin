package fr.nereide.editor.marker.java

import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiJavaToken
import fr.nereide.editor.marker.common.CommonServiceEcaMarkerProvider
import fr.nereide.project.pattern.OfbizJavaPatterns

class JavaServiceEcaEcaMarkerProvider extends CommonServiceEcaMarkerProvider {

    PsiElementPattern getPattern() { return OfbizJavaPatterns.SERVICE_CALL }

    Class getLeafElementType() { return PsiJavaToken.class }

}
