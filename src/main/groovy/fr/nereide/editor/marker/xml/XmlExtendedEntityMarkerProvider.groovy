package fr.nereide.editor.marker.xml

import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.xml.XmlToken
import fr.nereide.editor.marker.common.CommonEntityExtendMarkerProvider
import fr.nereide.project.pattern.OfbizXmlPatterns

class XmlExtendedEntityMarkerProvider extends CommonEntityExtendMarkerProvider {

    PsiElementPattern getPattern() { return OfbizXmlPatterns.ENTITY_OR_VIEW_CALL }

    Class getLeafElementType() { return XmlToken.class }
}
