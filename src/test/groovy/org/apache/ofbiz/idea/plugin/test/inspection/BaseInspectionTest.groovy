package org.apache.ofbiz.idea.plugin.test.inspection

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.codeInsight.intention.IntentionAction
import org.apache.ofbiz.idea.plugin.inspection.EmptyFileLocationInspection
import org.apache.ofbiz.idea.plugin.inspection.InspectionBundle
import org.apache.ofbiz.idea.plugin.test.BaseOfbizPluginTestCase

abstract class BaseInspectionTest extends BaseOfbizPluginTestCase {

    private static final LOCATION_QUICKFIX_NAME = InspectionBundle.message('inspection.location.target.file.not.found.use.quickfix')

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
        final IntentionAction action = myFixture.findSingleIntention(LOCATION_QUICKFIX_NAME)
        assertNotNull(action)
        myFixture.launchAction(action)
        myFixture.checkResultByFile("${getLang()}/${getTestName(false)}.after.${getLang()}")
    }
}

