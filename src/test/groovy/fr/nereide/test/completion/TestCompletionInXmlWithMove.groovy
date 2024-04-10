package fr.nereide.test.completion

class TestCompletionInXmlWithMove extends BaseComplTestCase {

    private static String MOVE_TO = "pilgrim/widget"

    @Override
    protected String getTestDataPath() {
        return "$BASE_TEST_DIR/xml"
    }

    protected String getDestination() { return MOVE_TO }

    void testTargetCompletionInForm() {
        doTest(['someTarget', 'some-other/target'], true)
    }

    void testTargetCompletionInScreenLink() {
        doTest(['someTarget', 'some-other/target'], true)
    }

//    void testTargetCompletionInFormLink() { }
//
//    void testTargetCompletionInMenuLink() { }
//
//    void testTargetCompletionInFormLinkInCpd() { }
//
//    void testTargetCompletionInMenuLinkInCpd() { }
}
