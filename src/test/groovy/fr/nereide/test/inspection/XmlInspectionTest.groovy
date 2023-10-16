package fr.nereide.test.inspection

import fr.nereide.inspection.xml.EmptyFileLocationInspection
import fr.nereide.inspection.InspectionBundle

class XmlInspectionTest extends BaseInspectionTest {

    String getLang() { return 'xml' }

    void testNoGroovyServiceFileFoundInspection() {
        String intention = InspectionBundle.message('inspection.location.target.file.not.found.use.quickfix')
        myFixture.enableInspections(new EmptyFileLocationInspection())
        doTest(intention)
    }
}
