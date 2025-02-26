package fr.nereide.test.inspection

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.codeInsight.intention.IntentionAction
import fr.nereide.inspection.xml.EmptyFileLocationInspection
import fr.nereide.inspection.xml.FormNotFoundInFileLocationInspection
import fr.nereide.inspection.xml.LabelNotFoundInXmlInspection
import fr.nereide.inspection.xml.ScreenNotFoundInFileLocationInspection
import fr.nereide.project.ProjectServiceInterface

import static fr.nereide.inspection.InspectionBundle.*

class XmlInspectionTest extends BaseInspectionTest {

    String getLang() { return 'xml' }

    //==============================
    // GROOVY TESTS
    //==============================
    void testNoGroovyServiceFileFoundInspectionFileFix() {
        String intention = message('inspection.location.target.file.not.found.use.quickfix.fixpath')
        myFixture.enableInspections(new EmptyFileLocationInspection())
        doInspectionThenQuickFixTestWithFileEdit(true, intention, null)
    }

    void testNoGroovyServiceFileFoundInspectionFileCreate() {
        String intention = message('inspection.location.target.file.not.found.use.quickfix.createfile')
        String description = message('inspection.location.target.file.not.found.display.descriptor')
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
                message('inspection.screen.not.found.on.target.use.quickfix.create'),
                message('inspection.screen.not.found.on.target.display.descriptor'),
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
        doMove()
        doScreenNotFoundTest(false, null, null)
    }

    void testScreenIsFoundInCompoundFileFromScreenWhenThereIsntButThereIsAFormWithSameName() {
        doMove()
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
                message('inspection.form.not.found.on.target.use.quickfix.create'),
                message('inspection.form.not.found.on.target.display.descriptor'),
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

    void testLabelNotFoundInScreenFile() {
        String desc = message('inspection.label.not.found.display.descriptor')
        String intention = message('inspection.label.not.found.quickfix.create')
        myFixture.enableInspections(new LabelNotFoundInXmlInspection())

        // Move file so that test component is found for label file searching
        String file = "${this.getTestName(false)}.xml"
        String dest = 'zelda/widget'
        myFixture.moveFile("xml/$file", dest)

        myFixture.configureByFile("$dest/$file")
        List<HighlightInfo> highlightInfos = myFixture.doHighlighting()
        List<String> highlightDescs = highlightInfos.collect { it.description }
        assertFalse highlightInfos.isEmpty()
        assert highlightDescs.contains(desc)
        final IntentionAction action = myFixture.findSingleIntention(intention)
        assertNotNull action
        myFixture.launchAction(action)

        assert myFixture.getProject().getService(ProjectServiceInterface.class).getProperty('notExistingLabel')

//        PsiFile fileToLookIn = expectedFileLocation ? getExpectedFile(expectedFileLocation) : myFixture.getFile()
//        List<XmlTag> tags = PsiTreeUtil.collectElements(fileToLookIn, getTagFilter())
//        assert tags.any { XmlTag tag ->
//            tag.getAttribute('name')?.value == elName && tag.getName() == elType
//        }
    }

}
