package fr.nereide.editor.marker.java

import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import fr.nereide.editor.marker.common.CommonEntityExtendMarkerProvider
import fr.nereide.project.pattern.OfbizJavaPatterns

class JavaExtendedEntityMarkerProvider extends CommonEntityExtendMarkerProvider {

    PsiElementPattern getPattern() { return OfbizJavaPatterns.ENTITY_CALL }

    Class getLeafElementType() { return PsiElement.class }
}
