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

class TestReferenceInXmlCompound extends GenericRefTestCase {

    private static String MOVE_TO = "FooComponent/widget"

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/testData/compound"
    }

    protected void doTest(Class expectedClass, String expectedName) {
        doTest(expectedClass, expectedName, true)
    }

    protected void doTest(Class expectedClass, String expectedName, boolean strict) {
        String file = "${this.getTestName(false).replaceAll('test', '')}.xml"
        myFixture.moveFile(file, MOVE_TO)
        configureByFileAndTestRefTypeAndValue(file, expectedClass, expectedName, strict)
    }

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
        doTest(FileReference.class, 'footemplate', false)
    }

    void testCpdScreenRefFromExtScreen() {
        doTest(ScreenReference.class, 'MyScreenInCpd', false)
    }

    void testCpdFormRefFromExtScreen() {
        doTest(FormReference.class, 'MyFormInCpd', false)
    }

    //=====================================
    //              FORMS TESTS
    //=====================================

    void testCpdRequestMapRefFromCpdForm() {
        doTest(RequestMapReference.class, 'MyFooRequest')
    }

    void testCpdScreenRefFromCpdFieldForm() {
        doTest(ScreenReference.class, 'MyPitaScreen', false)
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

    //=====================================
    //         REQUEST MAP TESTS
    //=====================================

    void testCpdScreenRefFromCpdViewMap() {
        doTest(ScreenReference.class, 'MyFooScreenInCpd', false)
    }
}
