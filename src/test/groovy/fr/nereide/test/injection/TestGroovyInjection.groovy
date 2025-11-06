package fr.nereide.test.injection

import org.jetbrains.plugins.groovy.GroovyLanguage

/**
 * Injection test in groovy
 */
class TestGroovyInjection extends BaseInjectionTestCase {

    void testValueAttrInSetTagInScreen() {
        doTest()
    }

    void testValueAttrInSetTagInMenu() {
        doTest()
    }

    void testValueAttrInSetTagInForm() {
        doTest()
    }

    void testUseWhenAttrInFieldTagInForm() {
        doTest()
    }

    void testTooltipAttrInFieldTagInForm() {
        doTest()
    }

    void testDefaultValueAttrInFieldTagInForm() {
        doTest()
    }

    void testDefaultValueAttrInFieldTagInScreen() {
        doTest()
    }

    void testLocationAttrInIncludeScreenTagInScreen() {
        doTest()
    }

    void testDescriptionAttrInForm() {
        doTest()
    }

    @Override
    protected String getTestDataPath() {
        return 'src/test/resources/testData/injection/xml/groovy'
    }

    protected void doTest() {
        super.doTest(GroovyLanguage)
    }

}
