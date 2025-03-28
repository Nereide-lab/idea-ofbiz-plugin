package fr.nereide.test.inspection


import fr.nereide.inspection.xml.DuplicatedFormInspection
import fr.nereide.inspection.xml.DuplicatedMenuInspection
import fr.nereide.inspection.xml.DuplicatedScreenInspection
import fr.nereide.inspection.xml.DuplicatedUriInspection

import static fr.nereide.inspection.InspectionBundle.message

class XmlDuplicateInspectionTest extends BaseInspectionTest {

    String dest = 'zelda/widget'

    @Override
    String getLang() {
        return 'xml'
    }

    @Override
    protected void setUp() {
        super.setUp()
        String myTestFileName = "${this.getTestName(false)}.xml"
        myFixture.createFile(myTestFileName, '')
        myFixture.moveFile(myTestFileName, dest)
        myFixture.copyFileToProject("xml/$myTestFileName", "$dest/$myTestFileName")
    }


    //==============================
    // Duplicated URI
    //==============================
    void testDuplicatedUriInCurrentController() {
        String desc = message('inspection.uri.duplicate.display.descriptor')
        myFixture.enableInspections(new DuplicatedUriInspection())
        String file = "${this.getTestName(false)}.xml"
        myFixture.configureByFile("$dest/$file")
        doHighlightTest(true, desc)
    }

    //==============================
    // Duplicated Screen
    //==============================
    void testDuplicatedScreenInCurrentFile() {
        String desc = message('inspection.screen.duplicate.display.descriptor')
        String file = "${this.getTestName(false)}.xml"
        myFixture.configureByFile("$dest/$file")
        myFixture.enableInspections(new DuplicatedScreenInspection())
        doHighlightTest(true, desc)
    }

    void testDuplicatedScreenInCurrentCompoundFile() {
        String desc = message('inspection.screen.duplicate.display.descriptor')
        String file = "${this.getTestName(false)}.xml"
        myFixture.configureByFile("$dest/$file")
        myFixture.enableInspections(new DuplicatedScreenInspection())
        doHighlightTest(true, desc)
    }

    void testDuplicatedTargetScreen() {
         String desc = message('inspection.screen.duplicate.display.descriptor')
         String file = "${this.getTestName(false)}.xml"
         myFixture.configureByFile("$dest/$file")
         myFixture.enableInspections(new DuplicatedScreenInspection())
         doHighlightTest(true, desc)
    }

    // void testDuplicatedTargetScreenInCompoundFile() { TODO

    //==============================
    // Duplicated Form
    //==============================
    void testDuplicatedFormInCurrentFile() {
        String desc = message('inspection.form.duplicate.display.descriptor')
        String file = "${this.getTestName(false)}.xml"
        myFixture.configureByFile("$dest/$file")
        myFixture.enableInspections(new DuplicatedFormInspection())
        doHighlightTest(true, desc)
    }

    void testDuplicatedFormInCurrentCompoundFile() {
        String desc = message('inspection.form.duplicate.display.descriptor')
        String file = "${this.getTestName(false)}.xml"
        myFixture.configureByFile("$dest/$file")
        myFixture.enableInspections(new DuplicatedFormInspection())
        doHighlightTest(true, desc)
    }

    // void testDuplicatedTargetForm() { TODO
    // void testDuplicatedTargetFormInCompoundFile() { TODO

    //==============================
    // Duplicated Menu
    //==============================
    void testDuplicatedMenuInCurrentFile() {
        String desc = message('inspection.menu.duplicate.display.descriptor')
        String file = "${this.getTestName(false)}.xml"
        myFixture.configureByFile("$dest/$file")
        myFixture.enableInspections(new DuplicatedMenuInspection())
        doHighlightTest(true, desc)
    }

    void testDuplicatedMenuInCurrentCompoundFile() {
        String desc = message('inspection.menu.duplicate.display.descriptor')
        String file = "${this.getTestName(false)}.xml"
        myFixture.configureByFile("$dest/$file")
        myFixture.enableInspections(new DuplicatedMenuInspection())
        doHighlightTest(true, desc)
    }

    // void testDuplicatedTargetMenu() { TODO
    // void testDuplicatedTargetMenuInCompoundFile() { TODO
}
