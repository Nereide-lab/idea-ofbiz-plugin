package fr.nereide.test.templating

import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.TemplateManagerImpl
import com.intellij.codeInsight.template.impl.TemplateSettings
import com.intellij.codeInsight.template.impl.TemplateState
import com.intellij.openapi.editor.Editor
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
//        myFixture.copyDirectoryToProject('assets', '')
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

    void testGroovyServiceExpandInGroovyScript() {
        String file = "${this.getTestName(false)}.groovy"
        myFixture.configureByFile(file)
        Editor editor = myFixture.editor
        TemplateManager tm = TemplateManager.getInstance(myFixture.project)
        Template template = TemplateSettings.getInstance().getTemplate(RUN_SERVICE_TEMPL_NAME, GROOVY_OFB_TEMPL_GROUP)
        tm.startTemplate(editor, template)
        TemplateState state = TemplateManagerImpl.getTemplateState(editor)
        state.nextTab()
        tm.finishTemplate(editor)
    }
}
