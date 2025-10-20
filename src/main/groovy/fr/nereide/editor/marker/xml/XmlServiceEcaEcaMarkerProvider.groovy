package fr.nereide.editor.marker.xml

import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.xml.XmlToken
import fr.nereide.editor.marker.common.CommonServiceEcaMarkerProvider
import fr.nereide.project.pattern.OfbizXmlPatterns

/**
 * Xml Specific eca marker class
 */
class XmlServiceEcaEcaMarkerProvider extends CommonServiceEcaMarkerProvider {

    final PsiElementPattern pattern = OfbizXmlPatterns.SERVICE_DEF_CALL
    final Class leafElementType = XmlToken

}
