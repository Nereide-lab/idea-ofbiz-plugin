package fr.nereide.test.inspection


import fr.nereide.inspection.xml.EmptyFileLocationInspection
import fr.nereide.inspection.InspectionBundle
import fr.nereide.inspection.xml.FormNotFoundInFileLocationInspection
import fr.nereide.inspection.xml.ScreenNotFoundInFileLocationInspection

class XmlInspectionTest extends BaseInspectionTest {

    String getLang() { return 'xml' }

    //==============================
    // GROOVY TESTS
    //==============================
    void testNoGroovyServiceFileFoundInspectionFileFix() {
        String intention = InspectionBundle.message('inspection.location.target.file.not.found.use.quickfix.fixpath')
        myFixture.enableInspections(new EmptyFileLocationInspection())
        doInspectionThenQuickFixTestWithFileEdit(true, intention, null)
    }

    void testNoGroovyServiceFileFoundInspectionFileCreate() {
        String intention = InspectionBundle.message('inspection.location.target.file.not.found.use.quickfix.createfile')
        String description = InspectionBundle.message('inspection.location.target.file.not.found.display.descriptor')
        myFixture.enableInspections(new EmptyFileLocationInspection())
        String location = 'zelda/webcommon/WEB-INF/zelda-controller.xml'
        doFileInspectionTestWithFileCreation(true, intention, description, location)
    }

    //==============================
    // SCREEN TESTS
    //==============================
    void doScreenNotFoundTest(boolean mustFind, String location, String elName) {
        myFixture.enableInspections(new ScreenNotFoundInFileLocationInspection())
        doInspectionThenQuickFixWithXmlElementCreate(mustFind,
                InspectionBundle.message('inspection.screen.not.found.on.target.use.quickfix.create'),
                InspectionBundle.message('inspection.screen.not.found.on.target.display.descriptor'),
                location, elName, 'screen')
    }

    void testNoScreenFoundInTargetFileInScreenWithLocationAttr() {
        String location = 'zelda/widget/ZeldaFoesScreens.xml'
        String elementName = 'HobegobelinScreen'
        doScreenNotFoundTest(true, location, elementName)
    }

    void testNoScreenFoundInTargetFileInScreenWithoutLocationAttr() {
        String elementName = 'SomeOtherRandomScreen'
        doScreenNotFoundTest(true, null, elementName)
    }

    void testNoScreenFoundInTargetFileInForm() {
        String location = 'zelda/widget/ZeldaFoesScreens.xml'
        String elementName = 'SomeZeldaScreenInForm'
        doScreenNotFoundTest(true, location, elementName)
    }

    void testNoScreenFoundInTargetFileInController() {
        String location = 'zelda/widget/ZeldaFoesScreens.xml'
        String elementName = 'SomeZeldaScreenInController'
        doScreenNotFoundTest(true, location, elementName)
    }

    void testScreenIsFoundWithoutWarningInController() {
        doScreenNotFoundTest(false, null, null)
    }

    void testDynamicScreenNameIsIgnored() {
        doScreenNotFoundTest(false, null, null)
    }

    void testScreenIsFoundInCompoundFileFromScreen() {
        doCpd()
        doScreenNotFoundTest(false, null, null)
    }

    void testScreenIsFoundInCompoundFileFromScreenWhenThereIsntButThereIsAFormWithSameName() {
        doCpd()
        String location = 'zelda/widget/ScreenIsFoundInCompoundFileFromScreenWhenThereIsntButThereIsAFormWithSameName.xml'
        String elementName = 'ILikeFairPhone'
        doScreenNotFoundTest(true, location, elementName)
    }

    void testScreenAtDynamicLocationIsNoError() {
        doScreenNotFoundTest(false, null, null)
    }

    //==============================
    // FORMS TESTS
    //==============================
    void doFormNotFoundTest(boolean mustFind, String location, String elName) {
        myFixture.enableInspections(new FormNotFoundInFileLocationInspection())
        doInspectionThenQuickFixWithXmlElementCreate(mustFind,
                InspectionBundle.message('inspection.form.not.found.on.target.use.quickfix.create'),
                InspectionBundle.message('inspection.form.not.found.on.target.display.descriptor'),
                location, elName, 'form')
    }

    void testNoFormFoundInTargetFileInForm() {
        String location = 'zelda/widget/ZeldaFoesForms.xml'
        String elementName = 'MyZeldaFoesFormNotFound'
        doFormNotFoundTest(true, location, elementName)
    }

    void testNoFormFoundInSameFileInForm() {
        String elementName = 'IWantAFormInThisFile'
        doFormNotFoundTest(true, null, elementName)
    }

    void testNoFormFoundInTargetFileInScreen() {
        String location = 'zelda/widget/ZeldaFoesForms.xml'
        String elementName = 'AFormInADistantFileFormScreen'
        doFormNotFoundTest(true, location, elementName)
    }

    void testDynamicFormNameIsIgnored() {
        doFormNotFoundTest(false, null, null)
    }

    //==============================
    // UILABEL TESTS
    //==============================

}
