package fr.nereide.test.inspection

import com.intellij.codeInspection.LocalInspectionTool
import fr.nereide.inspection.xml.DuplicatedUriInspection

import static fr.nereide.inspection.InspectionBundle.message


class XmlUriDuplicateInspectionTest extends BaseInspectionTest {

    final String URI_DESC = message('inspection.uri.duplicate.display.descriptor')
    final LocalInspectionTool URI_INSP = new DuplicatedUriInspection()
    final String DEST = 'zelda/webapp/zelda'

    @Override
    String getLang() {
        return 'xml'
    }

    @Override
    protected void setUp() {
        super.setUp()
        String myTestFileName = "${this.getTestName(false)}.xml"
        myFixture.copyFileToProject("xml/$myTestFileName", "$DEST/$myTestFileName")
    }

    void doTest(boolean mustFind = true, String desc, LocalInspectionTool inspection) {
        myFixture.enableInspections(inspection)
        String file = "${this.getTestName(false)}.xml"
        myFixture.configureByFile("$DEST/$file")
        doHighlightTest(mustFind, desc)
    }

    void testDuplicatedUriInCurrentController() { doTest(URI_DESC, URI_INSP) }

    void testDuplicatedUriInCurrentControllerWithDifferentMethod() { doTest(false, URI_DESC, URI_INSP) }

    // TODO It's not a duplicate, it's an overload. Should be in a dedicated inspection, with info level
//    void testDuplicatedUriButWithDifferentMountPoints() { doTest(false, URI_DESC, URI_INSP) }

}
