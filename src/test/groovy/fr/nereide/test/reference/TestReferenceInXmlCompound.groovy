package fr.nereide.test.reference

import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.impl.PsiMultiReference
import fr.nereide.reference.xml.FormReference
import fr.nereide.test.GenericOfbizPluginTestCase

class TestReferenceInXmlCompound extends GenericOfbizPluginTestCase {

    @Override
    protected void setUp() {
        super.setUp()
        myFixture.copyDirectoryToProject('assets', '')
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/testData/compound"
    }

    /**
     * Gestion des références dans les compounds :
     * Dans l'idéal, il faut à chaque fois que les références soient à double sens :
     *      Si j'inclut un écran extérieur dans un compound alors l'écran extérieur doit trouver l'écran du compound.
     * Dans un compound on peut avoir :
     *  - Des controlleurs (Request et View)
     *  - Des forms
     *  - Des menus
     *  - Des screens
     */
    //=====================================
    //              SCREEN TESTS
    //=====================================

    void testCpdFormReferenceFromCpdScreen() {
        String moveTo = "FooComponent/widget"
        String file = 'CpdFormReferenceFromCpdScreen.xml'
        myFixture.moveFile(file, moveTo)
        myFixture.configureByFile(file)
        PsiReference ref = myFixture.getReferenceAtCaretPositionWithAssertion()
        if (ref instanceof PsiMultiReference) {
            ref = ref.getReferences().find { it instanceof FormReference }
        }
        assert ref instanceof FormReference
        assertEquals 'FooForm', ref.getElement().getName() as String
        assertNotNull ref.resolve()
    }

    void testCpdGridReferenceFromCpdScreen() {
        String file = 'CpdGridReferenceFromCpdScreen.xml'
        String moveTo = "FooComponent/widget"
        myFixture.moveFile(file, moveTo)
        myFixture.configureByFile(file)
        PsiReference ref = myFixture.getReferenceAtCaretPositionWithAssertion()
        if (ref instanceof PsiMultiReference) {
            ref = ref.getReferences().find { it instanceof FormReference }
        }
        assert ref instanceof FormReference
        assertEquals 'FooGrid', ref.getElement().getName() as String
        assertNotNull ref.resolve()
    }

    //TODO screen + decorator
    void testCpdScreenReferenceFromCpdScreen() {
        assert true
    }

    //TODO form et grid
    void testExternalFormRefFromCpdScreen() {
        assert true
    }

    //TODO screen + decorator
    void testExternalScreenRefFromCpdScreen() {
        assert true
    }

    //TODO
    void testCpdMenuRefFromCpdScreen() {
        assert true
    }

    //TODO
    void testExternalMenuRefFromCpdScreen() {
        assert true
    }

    // TODO same patterns than in standard xml
    void testEntityRefFromCpdScreen() {
        assert true
    }

    // TODO
    void testFileRefFromCpdScreen() {
        assert true
    }
}
