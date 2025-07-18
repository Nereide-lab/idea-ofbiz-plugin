package fr.nereide.test.editor


import com.intellij.psi.xml.XmlAttributeValue
import fr.nereide.editor.marker.xml.XmlEntityEcaMarkerProvider
import fr.nereide.editor.marker.xml.XmlExtendedEntityMarkerProvider
import fr.nereide.editor.marker.xml.XmlServiceEcaMarkerProvider

class XmlLineMarkerTests extends BaseLineMarkerTest {

    String getExtension() { return 'xml' }

    Class getElementTypeToFind() { return XmlAttributeValue.class }

    void testServiceEcaMarkerInXml() {
        doTest(new XmlServiceEcaMarkerProvider(), '1 ECA(s) present on service')
    }

    void testEntityEcaMarkerInXml() {
        doTest(new XmlEntityEcaMarkerProvider(), '1 ECA(s) present on entity')
    }

    void testExtendedEntityMarkerInXml() {
        doTest(new XmlExtendedEntityMarkerProvider(), 'Entity is extended')
    }
}
