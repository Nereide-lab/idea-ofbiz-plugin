package fr.nereide.test.inspection

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import fr.nereide.inspection.EmptyFileLocationInspection
import fr.nereide.test.BaseOfbizPluginTestCase

abstract class BaseInspectionTest extends BaseOfbizPluginTestCase {

    abstract String getLang()

    @Override
    protected void setUp() {
        super.setUp()
        myFixture.copyDirectoryToProject('assets', '')
        myFixture.enableInspections(new EmptyFileLocationInspection())
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/testData/inspection"
    }

    protected void doTest() {
        myFixture.configureByFile("${getLang()}/${getTestName(false)}.${getLang()}")
        List<HighlightInfo> highlightInfos = myFixture.doHighlighting()
        assertFalse(highlightInfos.isEmpty())

        /* TODO : next step
        // Get the quick fix action for comparing references inspection and apply it to the file
        final IntentionAction action = myFixture.findSingleIntention(QUICK_FIX_NAME)
        assertNotNull(action);
        myFixture.launchAction(action)
        // Verify the results
        myFixture.checkResultByFile(testName + ".after.java");
        */
    }

}
