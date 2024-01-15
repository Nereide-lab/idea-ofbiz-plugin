package fr.nereide.test.inspection

import fr.nereide.inspection.xml.EmptyFileLocationInspection
import fr.nereide.inspection.InspectionBundle
import fr.nereide.inspection.xml.FormNotFoundInFileLocation
import fr.nereide.inspection.xml.ScreenNotFoundInFileLocation

class XmlInspectionTest extends BaseInspectionTest {

    String getLang() { return 'xml' }

    //==============================
    // GROOVY TESTS
    //==============================
    void testNoGroovyServiceFileFoundInspectionFileFix() {
        String intention = InspectionBundle.message('inspection.location.target.file.not.found.use.quickfix.fixpath')
        myFixture.enableInspections(new EmptyFileLocationInspection())
        doFileFixTest(intention)
    }

    void testNoGroovyServiceFileFoundInspectionFileCreate() {
        String intention = InspectionBundle.message('inspection.location.target.file.not.found.use.quickfix.createfile')
        myFixture.enableInspections(new EmptyFileLocationInspection())
        String location = 'zelda/webcommon/WEB-INF/zelda-controller.xml'
        doFileInspectionTest(intention, location, true)
    }

    //==============================
    // SCREEN TESTS
    //==============================
    void testNoScreenFoundInTargetFileInScreenWithLocationAttr() {
        String intention = InspectionBundle.message('inspection.screen.not.found.on.target.use.quickfix.create')
        myFixture.enableInspections(new ScreenNotFoundInFileLocation())
        String location = 'zelda/widget/ZeldaFoesScreens.xml'
        String elementName = 'HobegobelinScreen'
        String desc = InspectionBundle.message('inspection.screen.not.found.on.target.display.descriptor')
        doScreenInspectionTest(intention, desc, location, elementName, true)
    }

    void testNoScreenFoundInTargetFileInScreenWithoutLocationAttr() {
        String intention = InspectionBundle.message('inspection.screen.not.found.on.target.use.quickfix.create')
        myFixture.enableInspections(new ScreenNotFoundInFileLocation())
        String elementName = 'SomeOtherRandomScreen'
        String desc = InspectionBundle.message('inspection.screen.not.found.on.target.display.descriptor')
        doScreenInspectionTest(intention, desc, elementName, true)
    }

    void testNoScreenFoundInTargetFileInForm() {
        String intention = InspectionBundle.message('inspection.screen.not.found.on.target.use.quickfix.create')
        myFixture.enableInspections(new ScreenNotFoundInFileLocation())
        String location = 'zelda/widget/ZeldaFoesScreens.xml'
        String elementName = 'SomeZeldaScreenInForm'
        String desc = InspectionBundle.message('inspection.screen.not.found.on.target.display.descriptor')
        doScreenInspectionTest(intention, desc, location, elementName, true)
    }

    void testNoScreenFoundInTargetFileInController() {
        String intention = InspectionBundle.message('inspection.screen.not.found.on.target.use.quickfix.create')
        myFixture.enableInspections(new ScreenNotFoundInFileLocation())
        String location = 'zelda/widget/ZeldaFoesScreens.xml'
        String elementName = 'SomeZeldaScreenInController'
        String desc = InspectionBundle.message('inspection.screen.not.found.on.target.display.descriptor')
        doScreenInspectionTest(intention, desc, location, elementName, true)
    }

    void testScreenIsFoundWithoutWarningInController() {
        myFixture.enableInspections(new ScreenNotFoundInFileLocation())
        String desc = InspectionBundle.message('inspection.screen.not.found.on.target.display.descriptor')
        doScreenInspectionTest(null, desc, null, null, false)
    }

    void testDynamicScreenNameIsIgnored() {
        myFixture.enableInspections(new ScreenNotFoundInFileLocation())
        String desc = InspectionBundle.message('inspection.screen.not.found.on.target.display.descriptor')
        doScreenInspectionTest(null, desc, null, null, false)
    }

    void testScreenIsFoundInCompoundFileFromScreen() {
        myFixture.enableInspections(new ScreenNotFoundInFileLocation())
        String desc = InspectionBundle.message('inspection.screen.not.found.on.target.display.descriptor')
        doScreenInspectionTestInCompound(null, desc, null, null, false)
    }

    /**
     * Case of a screen called in a cpd, when this screen doesn't exists but there is a form with tha name
     */
    void testScreenIsFoundInCompoundFileFromScreenWhenThereIsntButThereIsAFormWithSameName() {
        String intention = InspectionBundle.message('inspection.screen.not.found.on.target.use.quickfix.create')
        myFixture.enableInspections(new ScreenNotFoundInFileLocation())
        String desc = InspectionBundle.message('inspection.screen.not.found.on.target.display.descriptor')
        String location = 'zelda/widget/ScreenIsFoundInCompoundFileFromScreenWhenThereIsntButThereIsAFormWithSameName.xml'
        String elementName = 'ILikeFairPhone'
        doScreenInspectionTestInCompound(intention, desc, location, elementName, true)
    }

    void testScreenAtDynamicLocationIsNoError() {
        myFixture.enableInspections(new ScreenNotFoundInFileLocation())
        String desc = InspectionBundle.message('inspection.screen.not.found.on.target.display.descriptor')
        doScreenInspectionTest(null, desc, null, null, false)
    }

    //==============================
    // FORMS TESTS
    //==============================
    void testNoFormFoundInTargetFileInForm() {
        String intention = InspectionBundle.message('inspection.form.not.found.on.target.use.quickfix.create')
        myFixture.enableInspections(new FormNotFoundInFileLocation())
        String location = 'zelda/widget/ZeldaFoesForms.xml'
        String elementName = 'MyZeldaFoesFormNotFound'
        String desc = InspectionBundle.message('inspection.form.not.found.on.target.display.descriptor')
        doFormInspectionTest(intention, desc, location, elementName, true)
    }

    void testNoFormFoundInSameFileInForm() {
        String intention = InspectionBundle.message('inspection.form.not.found.on.target.use.quickfix.create')
        myFixture.enableInspections(new FormNotFoundInFileLocation())
        String elementName = 'IWantAFormInThisFile'
        String desc = InspectionBundle.message('inspection.form.not.found.on.target.display.descriptor')
        doFormInspectionTest(intention, desc, elementName, true)
    }

    void testNoFormFoundInTargetFileInScreen() {
        String intention = InspectionBundle.message('inspection.form.not.found.on.target.use.quickfix.create')
        myFixture.enableInspections(new FormNotFoundInFileLocation())
        String location = 'zelda/widget/ZeldaFoesForms.xml'
        String elementName = 'AFormInADistantFileFormScreen'
        String desc = InspectionBundle.message('inspection.form.not.found.on.target.display.descriptor')
        doFormInspectionTest(intention, desc, location, elementName, true)
    }

    void testDynamicFormNameIsIgnored() {
        myFixture.enableInspections(new FormNotFoundInFileLocation())
        String desc = InspectionBundle.message('inspection.form.not.found.on.target.display.descriptor')
        doFormInspectionTest(null, desc, null, null, false)
    }
}
