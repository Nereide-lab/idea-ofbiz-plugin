package fr.nereide.editor.marker.xml

import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.xml.XmlToken
import fr.nereide.editor.marker.BaseServiceEcaMarker
import fr.nereide.project.pattern.OfbizXmlPatterns

class XmlServiceEcaMarkerProvider extends BaseServiceEcaMarker {

    PsiElementPattern getPattern() {
        return OfbizXmlPatterns.SERVICE_DEF_CALL
    }

    Class getLeafElementType() {
        XmlToken.class
    }
}
