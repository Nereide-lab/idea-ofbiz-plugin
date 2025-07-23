package fr.nereide.editor.marker.xml

import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.xml.XmlToken
import fr.nereide.editor.marker.common.CommonServiceEcaMarkerProvider
import fr.nereide.project.pattern.OfbizXmlPatterns

class XmlServiceEcaEcaMarkerProvider extends CommonServiceEcaMarkerProvider {

    PsiElementPattern getPattern() { return OfbizXmlPatterns.SERVICE_DEF_CALL }

    Class getLeafElementType() { XmlToken.class }

}
