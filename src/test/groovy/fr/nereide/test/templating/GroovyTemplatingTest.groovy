package fr.nereide.test.templating

import com.intellij.codeInsight.lookup.Lookup
import com.intellij.codeInsight.lookup.LookupManager
import com.intellij.codeInsight.lookup.impl.LookupImpl
import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.TemplateManagerImpl
import com.intellij.codeInsight.template.impl.TemplateSettings
import com.intellij.codeInsight.template.impl.TemplateState
import com.intellij.codeInsight.template.impl.actions.ListTemplatesAction
import com.intellij.openapi.editor.Editor
import com.intellij.testFramework.LiveTemplateTestUtil
import com.intellij.testFramework.PlatformTestUtil
import com.intellij.util.ui.UIUtil
import fr.nereide.test.BaseOfbizPluginTestCase

import static fr.nereide.liveTemplates.TemplateConst.getGROOVY_OFB_TEMPL_GROUP
import static fr.nereide.liveTemplates.TemplateConst.getRUN_SERVICE_TEMPL_NAME

/**
 * Heavily inspired from {@code com.intellij.java.codeInsight.template.LiveTemplateTestCase}
 */
class GroovyTemplatingTest extends BaseOfbizPluginTestCase {

    @Override
    protected void setUp() {
        super.setUp()
        TemplateManagerImpl.setTemplateTesting(myFixture.getTestRootDisposable())
    }

    protected TemplateState getState() {
        Editor editor = myFixture.getEditor()
        return editor ? null : TemplateManagerImpl.getTemplateState(editor)
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/testData/templating"
    }

    String getExpectedFileLocation() {
        return "${getTestDataPath()}/reference/${getTestName(false)}.groovy.expected"
    }

    void testGroovyServiceExpandInGroovyScript() {
        String file = "${this.getTestName(false)}.groovy"
        myFixture.configureByFile(file)
        new ListTemplatesAction().actionPerformedImpl(myFixture.editor.project, myFixture.editor)
        (LookupManager.getActiveLookup(myFixture.editor) as LookupImpl).finishLookup(Lookup.NORMAL_SELECT_CHAR)
        assertSameLinesWithFile(getExpectedFileLocation(), myFixture.file.text )
    }
}
