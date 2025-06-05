package fr.nereide.editor.marker.xml

import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.xml.XmlToken
import fr.nereide.editor.marker.common.CommonEntityMarkerProvider
import fr.nereide.project.pattern.OfbizXmlPatterns

class XmlEntityEcaMarkerProvider extends CommonEntityMarkerProvider {

    PsiElementPattern getPattern() {
        return OfbizXmlPatterns.ENTITY_OR_VIEW_CALL
    }

    Class getLeafElementType() {
        XmlToken.class
    }

}
