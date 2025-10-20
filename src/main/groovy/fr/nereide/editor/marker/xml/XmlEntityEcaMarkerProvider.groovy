package fr.nereide.editor.marker.xml

import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.xml.XmlToken
import fr.nereide.editor.marker.common.CommonEntityEcaMarkerProvider
import fr.nereide.project.pattern.OfbizXmlPatterns

/**
 * Sub class for ECA Marking
 */
class XmlEntityEcaMarkerProvider extends CommonEntityEcaMarkerProvider {

    final PsiElementPattern pattern = OfbizXmlPatterns.ENTITY_OR_VIEW_CALL
    final Class leafElementType = XmlToken

}
