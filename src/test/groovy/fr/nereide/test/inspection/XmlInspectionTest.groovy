package fr.nereide.test.inspection

class XmlInspectionTest extends BaseInspectionTest {

    String getLang() { return 'xml' }

    void testNoGroovyServiceFileFoundInspection() {
        doTest()
    }
}
