package fr.nereide.test.editor

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.psi.PsiElement
import fr.nereide.test.BaseOfbizPluginTestCase
import org.junit.Ignore

@Ignore('Parent class, No tests here')
class BaseLineMarkerTest extends BaseOfbizPluginTestCase {

    static final String BASE_TEST_DIR = 'src/test/resources/testData/editor'
    static final String ELEMENT_SUFFIX = 'Element'

    @Override
    protected String getTestDataPath() {
        return "$BASE_TEST_DIR"
    }

    protected Class getElementTypeToFind() {
        return null
    }

    protected String getExtension() { return 'xml' }

    @Override
    protected void setUp() {
        super.setUp()
        myFixture.copyDirectoryToProject('assets', '')
    }

    protected void doTest(LineMarkerProvider lineMarker, String tooltip) {
        String file = "${this.getExtension()}/${this.getTestName(false)}.${this.getExtension()}"
        myFixture.configureByFile(file)
        PsiElement element = myFixture.findElementByText("${this.getTestName(false)}$ELEMENT_SUFFIX", this.getElementTypeToFind())
        LineMarkerInfo lineMarkerInfo = lineMarker.getLineMarkerInfo(element)
        assertNotNull(lineMarkerInfo)
        assertEquals(tooltip, lineMarkerInfo.getLineMarkerTooltip())
    }
}
