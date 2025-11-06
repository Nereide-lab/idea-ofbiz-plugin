package fr.nereide.test.inspection

import static fr.nereide.inspection.InspectionBundle.message

import com.intellij.codeInspection.LocalInspectionTool
import fr.nereide.inspection.xml.DuplicatedUriInspection

/**
 * Inspection tests for duplicates in Xml, specifically Uri
 */
// codenarc-disable JUnitTestMethodWithoutAssert
class XmlUriDuplicateInspectionTest extends BaseInspectionTest {

    private static final String URI_DESC = message('inspection.uri.duplicate.display.descriptor')
    private static final LocalInspectionTool URI_INSP = new DuplicatedUriInspection()
    private static final String DEST = 'zelda/webapp/zelda'

    void testDuplicatedUriInCurrentController() { doTest(URI_DESC, URI_INSP) }

    void testDuplicatedUriInCurrentControllerWithDifferentMethod() { doTest(false, URI_DESC, URI_INSP) }

    private void doTest(boolean mustFind = true, String desc, LocalInspectionTool inspection) {
        myFixture.enableInspections(inspection)
        String file = "${this.getTestName(false)}.xml"
        myFixture.configureByFile("$DEST/$file")
        doHighlightTest(mustFind, desc)
    }

    @Override
    protected String getLang() {
        return 'xml'
    }

    @Override
    protected void setUp() {
        super.setUp()
        String myTestFileName = "${this.getTestName(false)}.xml"
        myFixture.copyFileToProject("xml/$myTestFileName", "$DEST/$myTestFileName")
    }

}
