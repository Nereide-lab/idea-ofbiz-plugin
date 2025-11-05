package fr.nereide.test.reference

import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import fr.nereide.reference.xml.GridReference
import fr.nereide.reference.xml.JavaMethodReference
import fr.nereide.reference.xml.RequestMapReference
import fr.nereide.reference.common.ServiceReference
import fr.nereide.reference.xml.ViewMapReference
import fr.nereide.reference.common.EntityReference
import fr.nereide.reference.xml.FormReference
import fr.nereide.reference.xml.MenuReference
import fr.nereide.reference.xml.ScreenReference

/**
 * Reference tests in xml, compound specific
 */
class TestReferenceInXmlCompound extends BaseReferenceTestCase {

    private static String MOVE_TO = 'FooComponent/widget'

    @Override
    protected String getTestDataPath() {
        return "$BASE_TEST_DIR/compound"
    }

    protected String getDestination() { return MOVE_TO }

    //=====================================
    //              SCREEN TESTS
    //=====================================

    void testCpdFormReferenceFromCpdScreen() {
        doTest(FormReference, 'FooForm')
    }

    void testCpdGridReferenceFromCpdScreen() {
        doTest(GridReference, 'MyCompoundElement')
    }

    void testCpdScreenReferenceFromCpdScreen() {
        doTest(ScreenReference, 'BarScreen')
    }

    void testCpdScreenDecoratorReferenceFromCpdScreen() {
        doTest(ScreenReference, 'FooDecoratorScreen')
    }

    void testExternalFormRefFromCpdScreen() {
        doTest(FormReference, 'OneFormAmongOthers')
    }

    void testExternalScreenRefFromCpdScreen() {
        doTest(ScreenReference, 'MundaneScreen')
    }

    void testCpdMenuRefFromCpdScreen() {
        doTest(MenuReference, 'FooMenu')
    }

    void testExternalMenuRefFromCpdScreen() {
        doTest(MenuReference, 'BarMenu')
    }

    void testEntityRefFromCpdScreen() {
        doTest(EntityReference, 'Entito')
    }

    void testFileRefFromCpdScreen() {
        doTest(FileReference, 'footemplate')
    }

    void testCpdScreenRefFromExtScreen() {
        doTest(ScreenReference, 'MyScreenInCpd')
    }

    void testCpdFormRefFromExtScreen() {
        doTest(FormReference, 'MyFormInCpd')
    }

    //=====================================
    //              FORMS TESTS
    //=====================================

    void testCpdRequestMapRefFromCpdForm() {
        doTest(RequestMapReference, 'MyFooRequest')
    }

    void testCpdScreenRefFromCpdFieldForm() {
        doTest(ScreenReference, 'MyPitaScreen')
    }

    void testCpdFormRefFromCpdFieldForm() {
        doTest(FormReference, 'MyPitaForm')
    }

    void testCpdMenuRefFromCpdFieldForm() {
        doTest(MenuReference, 'MyPitaMenu')
    }

    //=====================================
    //         REQUEST MAP TESTS
    //=====================================

    void testCpdViewMapRefFromCpdRequestMap() {
        doTest(ViewMapReference, 'myFooResponseInCpd')
    }

    void testServiceRefFromCpdRequestMap() {
        doTest(ServiceReference, 'DonateToQuadratureDuNet')
    }

    void testJavaEventRefFromCpdRequestMap() {
        doTest(JavaMethodReference, 'login')
    }

    void testCpdScreenRefFromCpdViewMap() {
        doTest(ScreenReference, 'MyFooScreenInCpd')
    }

}
