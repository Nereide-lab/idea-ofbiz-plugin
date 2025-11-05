package fr.nereide.test.editor

import com.intellij.psi.xml.XmlAttributeValue
import fr.nereide.editor.marker.xml.XmlEntityEcaMarkerProvider
import fr.nereide.editor.marker.xml.XmlExtendedEntityMarkerProvider
import fr.nereide.editor.marker.xml.XmlServiceEcaEcaMarkerProvider

/**
 * Line markers and gutter icons tests in xml
 */
class XmlLineMarkerTests extends BaseLineMarkerTest {

    String getExtension() { return 'xml' }

    Class getElementTypeToFind() { return XmlAttributeValue }

    void testServiceEcaMarkerInXml() {
        doTest(new XmlServiceEcaEcaMarkerProvider(), '1 ECA(s) present on service')
    }

    void testEntityEcaMarkerInXml() {
        doTest(new XmlEntityEcaMarkerProvider(), '1 ECA(s) present on entity')
    }

    void testExtendedEntityMarkerInXml() {
        doTest(new XmlExtendedEntityMarkerProvider(), 'Entity is extended')
    }

}
