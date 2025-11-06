package fr.nereide.test.editor

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.psi.PsiElement
import fr.nereide.test.BaseOfbizPluginTestCase
import org.junit.Ignore

/**
 * Base class for line markers and gutter icons tests
 */
@Ignore('Parent class, No tests here')
abstract class BaseLineMarkerTest extends BaseOfbizPluginTestCase {

    protected static final String BASE_TEST_DIR = 'src/test/resources/testData/editor'
    protected static final String ELEMENT_SUFFIX = 'Element'

    protected abstract Class getElementTypeToFind()

    protected abstract String getExtension()

    @Override
    protected String getTestDataPath() {
        return BASE_TEST_DIR
    }

    @Override
    protected void setUp() {
        super.setUp()
        myFixture.copyDirectoryToProject('assets', '')
    }

    protected void doTest(LineMarkerProvider lineMarker, String tooltip) {
        String file = "${this.extension}/${this.getTestName(false)}.${this.extension}"
        myFixture.configureByFile(file)
        PsiElement element = myFixture.findElementByText("${this.getTestName(false)}$ELEMENT_SUFFIX",
                this.elementTypeToFind)
        LineMarkerInfo lineMarkerInfo = lineMarker.getLineMarkerInfo(element)
        assert lineMarkerInfo
        assert tooltip, lineMarkerInfo.lineMarkerTooltip
    }

}
