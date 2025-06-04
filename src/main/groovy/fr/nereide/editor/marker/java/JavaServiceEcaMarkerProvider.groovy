package fr.nereide.editor.marker.java

import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiJavaToken
import fr.nereide.editor.marker.common.CommonServiceMarkerProvider
import fr.nereide.project.pattern.OfbizJavaPatterns

class JavaServiceEcaMarkerProvider extends CommonServiceMarkerProvider {

    PsiElementPattern getPattern() {
        return OfbizJavaPatterns.SERVICE_CALL
    }

    Class getLeafElementType() {
        return PsiJavaToken.class
    }

}
