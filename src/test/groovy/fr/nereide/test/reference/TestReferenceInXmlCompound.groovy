package fr.nereide.test.reference

import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import fr.nereide.reference.xml.RequestMapReference
import fr.nereide.reference.xml.ServiceReference
import fr.nereide.reference.xml.ViewMapReference
import fr.nereide.reference.xml.EntityReference
import fr.nereide.reference.xml.FormReference
import fr.nereide.reference.xml.MenuReference
import fr.nereide.reference.xml.ScreenReference

class TestReferenceInXmlCompound extends GenericRefTestCase {

    private static String MOVE_TO = "FooComponent/widget"

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/testData/compound"
    }

    private void configureAndMoveFileAndTestRefTypeAndValue(String file, Class expectedRefType, String expectedRefValueName) {
        configureAndMoveFileAndTestRefTypeAndValue(file, expectedRefType, expectedRefValueName, true)
    }

    private void configureAndMoveFileAndTestRefTypeAndValue(String file, Class expectedRefType, String expectedRefValueName, boolean strict) {
        myFixture.moveFile(file, MOVE_TO)
        configureByFileAndTestRefTypeAndValueForXml(file, expectedRefType, expectedRefValueName, strict)
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
        configureAndMoveFileAndTestRefTypeAndValue(file, FormReference.class, 'FooForm')
    }

    void testCpdGridReferenceFromCpdScreen() {
        String file = 'CpdGridReferenceFromCpdScreen.xml'
        configureAndMoveFileAndTestRefTypeAndValue(file, FormReference.class, 'FooGrid')
    }

    void testCpdScreenReferenceFromCpdScreen() {
        String file = 'CpdScreenReferenceFromCpdScreen.xml'
        configureAndMoveFileAndTestRefTypeAndValue(file, ScreenReference.class, 'BarScreen')
    }

    void testCpdScreenDecoratorReferenceFromCpdScreen() {
        String file = 'CpdScreenDecoratorReferenceFromCpdScreen.xml'
        configureAndMoveFileAndTestRefTypeAndValue(file, ScreenReference.class, 'FooDecoratorScreen')
    }

    void testExternalFormRefFromCpdScreen() {
        String file = 'ExternalFormRefFromCpdScreen.xml'
        configureAndMoveFileAndTestRefTypeAndValue(file, FormReference.class, 'OneFormAmongOthers')
    }

    void testExternalScreenRefFromCpdScreen() {
        String file = 'ExternalScreenRefFromCpdScreen.xml'
        configureAndMoveFileAndTestRefTypeAndValue(file, ScreenReference.class, 'MundaneScreen')
    }

    void testCpdMenuRefFromCpdScreen() {
        String file = 'CpdMenuRefFromCpdScreen.xml'
        configureAndMoveFileAndTestRefTypeAndValue(file, MenuReference.class, 'FooMenu')
    }

    void testExternalMenuRefFromCpdScreen() {
        String file = 'ExternalMenuRefFromCpdScreen.xml'
        configureAndMoveFileAndTestRefTypeAndValue(file, MenuReference.class, 'BarMenu')
    }

    void testEntityRefFromCpdScreen() {
        String file = 'EntityRefFromCpdScreen.xml'
        configureAndMoveFileAndTestRefTypeAndValue(file, EntityReference.class, 'Entito')
    }

    void testFileRefFromCpdScreen() {
        String file = 'FileRefFromCpdScreen.xml'
        configureAndMoveFileAndTestRefTypeAndValue(file, FileReference.class, 'footemplate', false)
    }

    //=====================================
    //              FORMS TESTS
    //=====================================

    void testCpdRequestMapRefFromCpdForm() {
        String file = 'CpdRequestMapRefFromCpdForm.xml'
        configureAndMoveFileAndTestRefTypeAndValue(file, RequestMapReference.class, 'MyFooRequest')
    }

    void testCpdViewMapRefFromCpdRequestMap() {
        String file = 'CpdViewMapRefFromCpdRequestMap.xml'
        configureAndMoveFileAndTestRefTypeAndValue(file, ViewMapReference.class, 'myFooResponseInCpd')
    }

    void testServiceRefFromCpdRequestMap() {
        String file = 'ServiceRefFromCpdRequestMap.xml'
        configureAndMoveFileAndTestRefTypeAndValue(file, ServiceReference.class, 'DonateToQuadratureDuNet')
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
