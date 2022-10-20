package fr.nereide.test.reference

import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.impl.PsiMultiReference
import fr.nereide.reference.xml.ControllerRequestReference
import fr.nereide.reference.xml.EntityReference
import fr.nereide.reference.xml.FormReference
import fr.nereide.reference.xml.MenuReference
import fr.nereide.reference.xml.ScreenReference
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

    private void configureAndTestRefTypeAndValue(String file, Class expectedRefType, String expectedRefValueName) {
        String moveTo = "FooComponent/widget"
        myFixture.moveFile(file, moveTo)
        myFixture.configureByFile(file)
        PsiReference ref = myFixture.getReferenceAtCaretPositionWithAssertion()
        if (ref instanceof PsiMultiReference) {
            ref = ref.getReferences().find { expectedRefType.isAssignableFrom(it.getClass()) }
        }
        assert expectedRefType.isAssignableFrom(ref.getClass())
        assertEquals expectedRefValueName, ref.getElement().getName() as String
        assertNotNull ref.resolve()
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
        String file = 'CpdFormReferenceFromCpdScreen.xml'
        configureAndTestRefTypeAndValue(file, FormReference.class, 'FooForm')
    }

    void testCpdGridReferenceFromCpdScreen() {
        String file = 'CpdGridReferenceFromCpdScreen.xml'
        configureAndTestRefTypeAndValue(file, FormReference.class, 'FooGrid')
    }

    void testCpdScreenReferenceFromCpdScreen() {
        String file = 'CpdScreenReferenceFromCpdScreen.xml'
        configureAndTestRefTypeAndValue(file, ScreenReference.class, 'BarScreen')
    }

    void testCpdScreenDecoratorReferenceFromCpdScreen() {
        String file = 'CpdScreenDecoratorReferenceFromCpdScreen.xml'
        configureAndTestRefTypeAndValue(file, ScreenReference.class, 'FooDecoratorScreen')
    }

    void testExternalFormRefFromCpdScreen() {
        String file = 'ExternalFormRefFromCpdScreen.xml'
        configureAndTestRefTypeAndValue(file, FormReference.class, 'OneFormAmongOthers')
    }

    void testExternalScreenRefFromCpdScreen() {
        String file = 'ExternalScreenRefFromCpdScreen.xml'
        configureAndTestRefTypeAndValue(file, ScreenReference.class, 'MundaneScreen')
    }

    void testCpdMenuRefFromCpdScreen() {
        String file = 'CpdMenuRefFromCpdScreen.xml'
        configureAndTestRefTypeAndValue(file, MenuReference.class, 'FooMenu')
    }

    void testExternalMenuRefFromCpdScreen() {
        String file = 'ExternalMenuRefFromCpdScreen.xml'
        configureAndTestRefTypeAndValue(file, MenuReference.class, 'BarMenu')
    }

    void testEntityRefFromCpdScreen() {
        String file = 'EntityRefFromCpdScreen.xml'
        configureAndTestRefTypeAndValue(file, EntityReference.class, 'Entito')
    }

    void testCpdRequestMapRefFromCpdForm() {
        String file = 'CpdRequestMapRefFromCpdForm.xml'
        configureAndTestRefTypeAndValue(file, ControllerRequestReference.class, 'MyFooRequest')
    }

    // TODO
    void testFileRefFromCpdScreen() {
        assert true
    }

    // TODO
    void testCpdViewMapRefFromCpdRequestMap() {
        assert true
    }

    // TODO
    void testServiceRefFromCpdRequestMap() {
        assert true
    }

    // TODO
    void testJavaEventRefFromCpdRequestMap() {
        assert true
    }

    // TODO identique à location ?
    void testGroovyEventRefFromCpdRequestMap() {
        assert true
    }
}
