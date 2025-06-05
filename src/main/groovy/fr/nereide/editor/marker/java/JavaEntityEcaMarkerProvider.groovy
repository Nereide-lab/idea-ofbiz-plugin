package fr.nereide.editor.marker.java

import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiJavaToken
import fr.nereide.editor.marker.common.CommonEntityMarkerProvider
import fr.nereide.project.pattern.OfbizJavaPatterns

class JavaEntityEcaMarkerProvider extends CommonEntityMarkerProvider {

    PsiElementPattern getPattern() {
        return OfbizJavaPatterns.ENTITY_CALL
    }

    Class getLeafElementType() {
        return PsiJavaToken.class
    }

}
