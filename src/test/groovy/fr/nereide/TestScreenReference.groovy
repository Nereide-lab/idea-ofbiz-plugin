package fr.nereide

import com.intellij.psi.PsiReference
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase

class TestScreenReference extends LightJavaCodeInsightFixtureTestCase {
    TestScreenReference() {}

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/testData/ScreenReference"
    }

    void testScreenInCurrentFileReference() {
        myFixture.copyDirectoryToProject('ScreenInCurrentFileReference', '')
        PsiReference ref = myFixture.getReferenceAtCaretPositionWithAssertion("testDataRefScreenInCurrentFile.xml")
        assertEquals'FindFacility', ref.getElement().getName() as String
        assertNotNull ref.resolve()
    }

    void testScreenInDistantFileReference() {
        myFixture.copyDirectoryToProject('ScreenInDistantFileReference', '')
        PsiReference ref = myFixture.getReferenceAtCaretPositionWithAssertion("commonext/widget/ofbizsetup/SetupScreens.xml")
        assertEquals 'viewprofile', ref.getElement().getName() as String
        assertNotNull ref.resolve()
    }

    void testScreenNotFoundReferenceInCurrentFile() {
        myFixture.copyDirectoryToProject('ScreenNotFoundInCurrentFileReference', '')
        PsiReference ref = myFixture.getReferenceAtCaretPosition("testDataRefScreenInCurrentFile.xml")
        assertNull ref.resolve()
    }

    void testScreenNotFoundReferenceInDistantFile() {
        myFixture.copyDirectoryToProject('ScreenNotFoundInDistantFileReference', '')
        PsiReference ref = myFixture.getReferenceAtCaretPosition("commonext/widget/ofbizsetup/SetupScreens.xml")
        assertNull ref.resolve()
    }
}
