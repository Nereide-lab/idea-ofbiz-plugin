package fr.nereide.test.reference

import com.intellij.psi.PsiReference
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

    void testCpdFormReferenceFromCpdScreen() {
        String file = 'CpdFormReferenceFromCpdScreen.xml'
        myFixture.configureByFile(file)
        PsiReference ref = myFixture.getReferenceAtCaretPositionWithAssertion(file)
        assertEquals 'FooForm', ref.getElement().getName() as String
        assert ref instanceof FormReference
        assertNotNull ref.resolve()
    }
}
