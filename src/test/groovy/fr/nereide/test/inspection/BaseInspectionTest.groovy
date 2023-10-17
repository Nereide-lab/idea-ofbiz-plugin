package fr.nereide.test.inspection

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.vfs.VirtualFile
import fr.nereide.test.BaseOfbizPluginTestCase

abstract class BaseInspectionTest extends BaseOfbizPluginTestCase {

    abstract String getLang()

    String getExpectedFilePath() {
        return "${getLang()}/${getTestName(false)}.after.${getLang()}"
    }

    String getTestFile() {
        return "${getLang()}/${getTestName(false)}.${getLang()}"
    }

    @Override
    protected void setUp() {
        super.setUp()
        myFixture.copyDirectoryToProject('assets', '')
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/testData/inspection"
    }

    protected void doFileFixTest(String intention) {
        doFileFixTest(intention, null, true)
    }

    protected void doFileFixTest(String intention, String desc, boolean mustFind) {
        myFixture.configureByFile(testFile)
        List<HighlightInfo> highlightInfos = myFixture.doHighlighting()
        if (mustFind) {
            assertFalse(highlightInfos.isEmpty())
            final IntentionAction action = myFixture.findSingleIntention(intention)
            assertNotNull(action)
            myFixture.launchAction(action)
            myFixture.checkResultByFile(expectedFilePath, true)
        } else {
            List highlightDescs = highlightInfos.collect { it.description }
            assertFalse highlightDescs.contains(desc)
        }
    }

    protected void doFileCreateTest(String intention, String expectedFileLocation, boolean mustFind) {
        myFixture.configureByFile(testFile)
        List<HighlightInfo> highlightInfos = myFixture.doHighlighting()
        if (mustFind) {
            assertFalse highlightInfos.isEmpty()
            final IntentionAction action = myFixture.findSingleIntention(intention)
            assertNotNull action
            myFixture.launchAction(action)
            VirtualFile file = myFixture.getTempDirFixture().getFile(expectedFileLocation)
            assertNotNull file
        } else {
            assert true
        }
    }
}
