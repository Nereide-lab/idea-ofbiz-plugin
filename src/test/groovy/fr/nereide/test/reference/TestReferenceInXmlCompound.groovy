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

class TestReferenceInXmlCompound extends BaseReferenceTestCase {

    private static String MOVE_TO = "FooComponent/widget"

    @Override
    protected String getTestDataPath() {
        return "$BASE_TEST_DIR/compound"
    }

    protected String getDestination() { return MOVE_TO }

    //=====================================
    //              SCREEN TESTS
    //=====================================

    void testCpdFormReferenceFromCpdScreen() {
        doTest(FormReference.class, 'FooForm')
    }

    void testCpdGridReferenceFromCpdScreen() {
        doTest(GridReference.class, 'MyCompoundElement')
    }

    void testCpdScreenReferenceFromCpdScreen() {
        doTest(ScreenReference.class, 'BarScreen')
    }

    void testCpdScreenDecoratorReferenceFromCpdScreen() {
        doTest(ScreenReference.class, 'FooDecoratorScreen')
    }

    void testExternalFormRefFromCpdScreen() {
        doTest(FormReference.class, 'OneFormAmongOthers')
    }

    void testExternalScreenRefFromCpdScreen() {
        doTest(ScreenReference.class, 'MundaneScreen')
    }

    void testCpdMenuRefFromCpdScreen() {
        doTest(MenuReference.class, 'FooMenu')
    }

    void testExternalMenuRefFromCpdScreen() {
        doTest(MenuReference.class, 'BarMenu')
    }

    void testEntityRefFromCpdScreen() {
        doTest(EntityReference.class, 'Entito')
    }

    void testFileRefFromCpdScreen() {
        doTest(FileReference.class, 'footemplate')
    }

    void testCpdScreenRefFromExtScreen() {
        doTest(ScreenReference.class, 'MyScreenInCpd')
    }

    void testCpdFormRefFromExtScreen() {
        doTest(FormReference.class, 'MyFormInCpd')
    }

    //=====================================
    //              FORMS TESTS
    //=====================================

    void testCpdRequestMapRefFromCpdForm() {
        doTest(RequestMapReference.class, 'MyFooRequest')
    }

    void testCpdScreenRefFromCpdFieldForm() {
        doTest(ScreenReference.class, 'MyPitaScreen')
    }

    void testCpdFormRefFromCpdFieldForm() {
        doTest(FormReference.class, 'MyPitaForm')
    }

    void testCpdMenuRefFromCpdFieldForm() {
        doTest(MenuReference.class, 'MyPitaMenu')
    }

    //=====================================
    //         REQUEST MAP TESTS
    //=====================================

    void testCpdViewMapRefFromCpdRequestMap() {
        doTest(ViewMapReference.class, 'myFooResponseInCpd')
    }

    void testServiceRefFromCpdRequestMap() {
        doTest(ServiceReference.class, 'DonateToQuadratureDuNet')
    }

    void testJavaEventRefFromCpdRequestMap() {
        doTest(JavaMethodReference.class, 'login')
    }

    void testCpdScreenRefFromCpdViewMap() {
        doTest(ScreenReference.class, 'MyFooScreenInCpd')
    }
}
