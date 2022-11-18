package fr.nereide.test.reference

import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import fr.nereide.reference.xml.JavaMethodReference
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

    void testCpdScreenRefFromExtScreen() {
        String file = 'CpdScreenRefFromExtScreen.xml'
        configureAndMoveFileAndTestRefTypeAndValue(file, ScreenReference.class, 'MyScreenInCpd', false)
    }

    void testCpdFormRefFromExtScreen() {
        String file = 'CpdFormRefFromExtScreen.xml'
        configureAndMoveFileAndTestRefTypeAndValue(file, FormReference.class, 'MyFormInCpd', false)
    }
    //=====================================
    //              FORMS TESTS
    //=====================================

    void testCpdRequestMapRefFromCpdForm() {
        String file = 'CpdRequestMapRefFromCpdForm.xml'
        configureAndMoveFileAndTestRefTypeAndValue(file, RequestMapReference.class, 'MyFooRequest')
    }

    void testCpdScreenRefFromCpdFieldForm() {
        String file = 'CpdScreenRefFromCpdFieldForm.xml'
        configureAndMoveFileAndTestRefTypeAndValue(file, ScreenReference.class, 'MyPitaScreen', false)
    }

    void testCpdFormRefFromCpdFieldForm() {
        String file = 'CpdFormRefFromCpdFieldForm.xml'
        configureAndMoveFileAndTestRefTypeAndValue(file, FormReference.class, 'MyPitaForm')
    }

    void testCpdMenuRefFromCpdFieldForm() {
        String file = 'CpdMenuRefFromCpdFieldForm.xml'
        configureAndMoveFileAndTestRefTypeAndValue(file, MenuReference.class, 'MyPitaMenu')
    }
    //=====================================
    //         REQUEST MAP TESTS
    //=====================================

    void testCpdViewMapRefFromCpdRequestMap() {
        String file = 'CpdViewMapRefFromCpdRequestMap.xml'
        configureAndMoveFileAndTestRefTypeAndValue(file, ViewMapReference.class, 'myFooResponseInCpd')
    }

    void testServiceRefFromCpdRequestMap() {
        String file = 'ServiceRefFromCpdRequestMap.xml'
        configureAndMoveFileAndTestRefTypeAndValue(file, ServiceReference.class, 'DonateToQuadratureDuNet')
    }

    void testJavaEventRefFromCpdRequestMap() {
        String file = 'JavaEventRefFromCpdRequestMap.xml'
        configureAndMoveFileAndTestRefTypeAndValue(file, JavaMethodReference.class, 'login')
    }

    //=====================================
    //         REQUEST MAP TESTS
    //=====================================

    void testCpdScreenRefFromCpdViewMap() {
        String file = 'CpdScreenRefFromCpdViewMap.xml'
        configureAndMoveFileAndTestRefTypeAndValue(file, ScreenReference.class, 'MyFooScreenInCpd', false)
    }
}
