package fr.nereide.test.completion


/**
 * <pre>
 * {@code org.apache.ofbiz.entity.GenericValue paper = from('PaperCompany')
 * def foo = paper.<caret> }
 * </pre>
 */
class TestViewFieldsCompletion extends TestCompletionInGroovy {

    void testEntityFieldCompletionInGroovyFileWithViewNoNested() {
        List<String> expected = ['jo', 'gabriel', 'michael'],
                     notExpected = ['maline']
        doTest(expected, notExpected)
    }

    void testEntityFieldCompletionInGroovyFileWithViewAndNested() {
        List<String> expected = ['michael', 'jo', 'gabriel', 'david']
        doTest(expected)
    }

    void testEntityFieldCompletionInGroovyFileWithRecursiveView() {
        List<String> expected = ['david']
        doTest(expected)
    }

    void testEntityFieldCompletionInGroovyFileWithViewAndExclude() {
        List<String> expected = ['johnny', 'johnkreese'],
                     notExpected = ['daniel']
        doTest(expected, notExpected)
    }

    void testEntityFieldCompletionInGroovyFileWithViewAndExcludeViewField() {
        List<String> expected = ['johnny'],
                     notExpected = ['daniel', 'johnkreese']
        doTest(expected, notExpected)
    }

    void testEntityFieldCompletionInGroovyFileWithSimpleViewAndPrefix() {
        List<String> expected = ['bigshovelreese', 'bigshovelfrancis', 'bigshovellois', 'reese', 'francis', 'lois']
        doTest(expected)
    }

    void testEntityFieldCompletionInGroovyFileWithComplexViewAndPrefix() {
        List<String> expected = ['geniusbigshovelreese', 'geniusbigshovelfrancis', 'geniusbigshovellois',
                                 'geniusreese', 'geniusfrancis', 'geniuslois']
        doTest(expected)
    }
}
