package fr.nereide.test.injection

/**
 * Injection test in javascript
 */
class TestJavaScriptInjection extends BaseInjectionTestCase {

    @Override
    protected String getTestDataPath() {
        return 'src/test/resources/testData/injection/xml/javascript'
    }

    void testDummy() {
        assert true
    }

//    protected doTest() {
//        super.doTest(JavascriptLanguage)
//    }
//
//    void testActionAttrInForm() {
//        doTest()
//    }
//
//    void testTargetAttrInForm() {
//        doTest()
//    }
//
//    void testTargetAttrInMenu() {
//        doTest()
//    }

}
