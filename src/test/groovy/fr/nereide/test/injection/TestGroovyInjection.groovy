package fr.nereide.test.injection

import org.jetbrains.plugins.groovy.GroovyLanguage

class TestGroovyInjection extends BaseInjectionTestCase {

    @Override
    protected String getTestDataPath() {
        return 'src/test/resources/testData/injection/xml/groovy'
    }

    protected doTest() {
        super.doTest(GroovyLanguage.class)
    }

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
}