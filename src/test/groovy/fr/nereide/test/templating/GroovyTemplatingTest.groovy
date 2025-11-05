package fr.nereide.test.templating

import com.intellij.codeInsight.lookup.Lookup
import com.intellij.codeInsight.lookup.LookupManager
import com.intellij.codeInsight.lookup.impl.LookupImpl
import com.intellij.codeInsight.template.impl.TemplateManagerImpl
import com.intellij.codeInsight.template.impl.actions.ListTemplatesAction
import fr.nereide.test.BaseOfbizPluginTestCase

/**
 * Heavily inspired from {@code com.intellij.java.codeInsight.template.LiveTemplateTestCase}
 * and  {@code org.jetbrains.plugins.groovy.lang.GroovyLiveTemplatesTest }
 */
class GroovyTemplatingTest extends BaseOfbizPluginTestCase {

    @Override
    protected void setUp() {
        super.setUp()
        TemplateManagerImpl.setTemplateTesting(myFixture.testRootDisposable)
    }

    @Override
    protected String getTestDataPath() {
        return 'src/test/resources/testData/templating'
    }

    String getExpectedFileLocation() {
        return testDataPath + '/reference/' + getTestName(false) + '.groovy.expected'
    }

    void testGroovyServiceSimpleExpandInGroovyScript() {
        String file = "${this.getTestName(false)}.groovy"
        myFixture.configureByFile(file)
        new ListTemplatesAction().actionPerformedImpl(myFixture.editor.project, myFixture.editor)
        (LookupManager.getActiveLookup(myFixture.editor) as LookupImpl).finishLookup(Lookup.NORMAL_SELECT_CHAR)
        assertSameLinesWithFile(expectedFileLocation, myFixture.file.text)
    }

}
