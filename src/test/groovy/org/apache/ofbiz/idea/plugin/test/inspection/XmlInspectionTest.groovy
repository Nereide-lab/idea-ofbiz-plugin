package org.apache.ofbiz.idea.plugin.test.inspection

class XmlInspectionTest extends BaseInspectionTest {

    String getLang() { return 'xml' }

    void testNoGroovyServiceFileFoundInspection() {
        doTest()
    }
}
