package fr.nereide.test.inspection

import com.intellij.codeInspection.LocalInspectionTool
import fr.nereide.inspection.xml.DuplicatedEntityXmlInspection
import fr.nereide.inspection.xml.DuplicatedFormInspection
import fr.nereide.inspection.xml.DuplicatedMenuInspection
import fr.nereide.inspection.xml.DuplicatedScreenInspection
import fr.nereide.inspection.xml.DuplicatedServiceXmlInspection

import static fr.nereide.inspection.InspectionBundle.message

/**
 * Inspection tests for duplicates in Xml
 */
class XmlDuplicateInspectionTest extends BaseInspectionTest {

    final String FORM_DESC = message('inspection.form.duplicate.display.descriptor')
    final String SCREEN_DESC = message('inspection.screen.duplicate.display.descriptor')
    final String MENU_DESC = message('inspection.menu.duplicate.display.descriptor')
    final String SERVICE_DESC = message('inspection.service.duplicate.display.descriptor')
    final String ENTITY_DESC = message('inspection.entity.duplicate.display.descriptor')
    final LocalInspectionTool FORM_INSP = new DuplicatedFormInspection()
    final LocalInspectionTool SCREEN_INSP = new DuplicatedScreenInspection()
    final LocalInspectionTool MENU_INSP = new DuplicatedMenuInspection()
    final LocalInspectionTool SERVICE_INSP = new DuplicatedServiceXmlInspection()
    final LocalInspectionTool ENTITY_INSP = new DuplicatedEntityXmlInspection()

    final String DEST = 'zelda/widget'

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

    void testDuplicatedScreenInCurrentFile() { doTest(SCREEN_DESC, SCREEN_INSP) }

    void testDuplicatedScreenInCurrentCompoundFile() { doTest(SCREEN_DESC, SCREEN_INSP) }

    void testDuplicatedTargetScreen() { doTest(SCREEN_DESC, SCREEN_INSP) }

    void testDuplicatedTargetScreenInCompoundFile() { doTest(SCREEN_DESC, SCREEN_INSP) }

    void testDuplicatedFormInCurrentFile() { doTest(FORM_DESC, FORM_INSP) }

    void testDuplicatedFormInCurrentCompoundFile() { doTest(FORM_DESC, FORM_INSP) }

    void testDuplicatedTargetForm() { doTest(FORM_DESC, FORM_INSP) }

    void testDuplicatedTargetFormInCompoundFile() { doTest(FORM_DESC, FORM_INSP) }

    void testDuplicatedMenuInCurrentFile() { doTest(MENU_DESC, MENU_INSP) }

    void testDuplicatedMenuInCurrentCompoundFile() { doTest(MENU_DESC, MENU_INSP) }

    void testDuplicatedTargetMenu() { doTest(MENU_DESC, MENU_INSP) }

    void testDuplicatedTargetMenuInCompoundFile() { doTest(MENU_DESC, MENU_INSP) }

    void testDuplicatedServiceFromForm() { doTest(SERVICE_DESC, SERVICE_INSP) }

    void testDuplicatedEntityIsCalled() { doTest(ENTITY_DESC, ENTITY_INSP) }

    void testDuplicatedViewIsCalled() { doTest(ENTITY_DESC, ENTITY_INSP) }

}
