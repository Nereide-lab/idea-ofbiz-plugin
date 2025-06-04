package fr.nereide.test.editor


import com.intellij.psi.xml.XmlAttributeValue
import fr.nereide.editor.marker.xml.XmlEntityEcaMarkerProvider
import fr.nereide.editor.marker.xml.XmlServiceEcaMarkerProvider

class XmlLineMarkerTests extends BaseLineMarkerTest {

    protected String getExtension() { return 'xml' }

    @Override
    protected Class getElementTypeToFind() {
        return XmlAttributeValue.class
    }

    void testServiceEcaMarkerInXml() {
        doTest(new XmlServiceEcaMarkerProvider(), '1 ECA(s) present on service')
    }

    void testEntityEcaMarkerInXml() {
        doTest(new XmlEntityEcaMarkerProvider(), '1 ECA(s) present on entity')
    }
}
