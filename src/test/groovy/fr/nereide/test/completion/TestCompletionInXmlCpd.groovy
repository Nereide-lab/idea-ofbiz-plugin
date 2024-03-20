package fr.nereide.test.completion

class TestCompletionInXmlCpd extends BaseComplTestCase {

    private static String MOVE_TO = "forest-component/widget"

    @Override
    protected String getTestDataPath() {
        return "$BASE_TEST_DIR/compound"
    }

    protected String getDestination() { return MOVE_TO }

    void testScreenCompletionInIncludeScreenInCpd() {
        doTest(['Kelvin'])
    }

    void testScreenCompletionInIncludeDistantScreenInCpd() {
        doTest(['GregsScreen', 'KelvinScreen'])
    }

//    void testScreenCompletionInIncludeScreenInCpdFromController() {
//        doTest(['HufftonScreen', 'VirginiaScreen'])
//    }
}
