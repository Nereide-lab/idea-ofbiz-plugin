package fr.nereide.test.inspection

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.codeInsight.intention.IntentionAction
import fr.nereide.test.BaseOfbizPluginTestCase

abstract class BaseInspectionTest extends BaseOfbizPluginTestCase {

    abstract String getLang()

    @Override
    protected void setUp() {
        super.setUp()
        myFixture.copyDirectoryToProject('assets', '')
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/testData/inspection"
    }

    protected void doTest(String intention) {
        myFixture.configureByFile("${getLang()}/${getTestName(false)}.${getLang()}")
        List<HighlightInfo> highlightInfos = myFixture.doHighlighting()
        assertFalse(highlightInfos.isEmpty())
        final IntentionAction action = myFixture.findSingleIntention(intention)
        assertNotNull(action)
        myFixture.launchAction(action)
        myFixture.checkResultByFile("${getLang()}/${getTestName(false)}.after.${getLang()}")
    }
}
