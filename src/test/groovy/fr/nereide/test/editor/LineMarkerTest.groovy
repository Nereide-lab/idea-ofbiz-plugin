package fr.nereide.test.editor

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import fr.nereide.editor.marker.JavaServiceEcaMarkerProvider
import fr.nereide.test.BaseOfbizPluginTestCase

class LineMarkerTest extends BaseOfbizPluginTestCase {

    static final String BASE_TEST_DIR = 'src/test/resources/testData/editor'

    @Override
    protected String getTestDataPath() {
        return "$BASE_TEST_DIR"
    }

    protected String getExtension() { return 'xml' }

    protected String getDestination() { return null }

    @Override
    protected void setUp() {
        super.setUp()
        myFixture.copyDirectoryToProject('assets', '')
        if (getDestination()) {
            String file = "${this.getTestName(false)}.${getExtension()}"
            myFixture.moveFile(file, getDestination())
        }
    }

//    protected void doTest(Class expectedRefType, String expectedRefValueName) {
//        doTest(expectedRefType, expectedRefValueName, false)
//    }


    void testServiceEcaMarkerInJava() {
        myFixture.configureByFile("${this.getTestName(false)}.java")
        PsiElement method = myFixture.findElementByText('ServiceEcaMarkerInJavaService', PsiLiteralExpression.class)
        JavaServiceEcaMarkerProvider provider = new JavaServiceEcaMarkerProvider()
        LineMarkerInfo lineMarkerInfo = provider.getLineMarkerInfo(method)
        assertNotNull(lineMarkerInfo)


//        assertEquals(GutterIconRenderer.Alignment.RIGHT, lineMarkerInfo.getGutterIconRenderer().getAlignment())
//        assertEquals("My custom tooltip", lineMarkerInfo.getLineMarkerTooltip())
    }
}
