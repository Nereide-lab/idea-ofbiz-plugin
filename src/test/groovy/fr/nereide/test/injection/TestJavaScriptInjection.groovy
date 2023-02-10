package fr.nereide.test.injection

class TestJavaScriptInjection extends BaseInjectionTestCase {

    @Override
    protected String getTestDataPath() {
        return 'src/test/resources/testData/injection/xml/javascript'
    }

    void testDummy() {
        assert true
    }

//    protected doTest() {
//        super.doTest(JavascriptLanguage.class)
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
