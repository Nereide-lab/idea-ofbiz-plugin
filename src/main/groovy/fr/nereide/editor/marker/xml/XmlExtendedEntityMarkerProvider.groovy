package fr.nereide.editor.marker.xml

import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.xml.XmlToken
import fr.nereide.editor.marker.common.CommonEntityExtendMarkerProvider
import fr.nereide.project.pattern.OfbizXmlPatterns

/**
 * Groovy specific eca marker class
 */
class XmlExtendedEntityMarkerProvider extends CommonEntityExtendMarkerProvider {

    final PsiElementPattern pattern = OfbizXmlPatterns.ENTITY_OR_VIEW_CALL
    final Class leafElementType = XmlToken

}
