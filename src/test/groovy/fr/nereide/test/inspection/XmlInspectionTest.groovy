package fr.nereide.test.inspection

import fr.nereide.inspection.xml.EmptyFileLocationInspection
import fr.nereide.inspection.InspectionBundle

class XmlInspectionTest extends BaseInspectionTest {

    String getLang() { return 'xml' }

    void testNoGroovyServiceFileFoundInspectionFileFix() {
        String intention = InspectionBundle.message('inspection.location.target.file.not.found.use.quickfix.fixpath')
        myFixture.enableInspections(new EmptyFileLocationInspection())
        doFileFixTest(intention)
    }

    void testNoGroovyServiceFileFoundInspectionFileCreate() {
        String intention = InspectionBundle.message('inspection.location.target.file.not.found.use.quickfix.createfile')
        myFixture.enableInspections(new EmptyFileLocationInspection())
        String location = "zelda/webcommon/WEB-INF/zelda-controller.xml"
        doFileCreateTest(intention, location, true)
    }
}
