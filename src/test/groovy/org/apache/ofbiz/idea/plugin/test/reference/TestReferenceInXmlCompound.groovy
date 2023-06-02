package org.apache.ofbiz.idea.plugin.test.reference

import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import org.apache.ofbiz.idea.plugin.reference.xml.JavaMethodReference
import org.apache.ofbiz.idea.plugin.reference.xml.MenuReference

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
        doTest(org.apache.ofbiz.idea.plugin.reference.xml.FormReference.class, 'FooForm')
    }

    void testCpdGridReferenceFromCpdScreen() {
        doTest(org.apache.ofbiz.idea.plugin.reference.xml.GridReference.class, 'MyCompoundElement')
    }

    void testCpdScreenReferenceFromCpdScreen() {
        doTest(org.apache.ofbiz.idea.plugin.reference.xml.ScreenReference.class, 'BarScreen')
    }

    void testCpdScreenDecoratorReferenceFromCpdScreen() {
        doTest(org.apache.ofbiz.idea.plugin.reference.xml.ScreenReference.class, 'FooDecoratorScreen')
    }

    void testExternalFormRefFromCpdScreen() {
        doTest(org.apache.ofbiz.idea.plugin.reference.xml.FormReference.class, 'OneFormAmongOthers')
    }

    void testExternalScreenRefFromCpdScreen() {
        doTest(org.apache.ofbiz.idea.plugin.reference.xml.ScreenReference.class, 'MundaneScreen')
    }

    void testCpdMenuRefFromCpdScreen() {
        doTest(MenuReference.class, 'FooMenu')
    }

    void testExternalMenuRefFromCpdScreen() {
        doTest(MenuReference.class, 'BarMenu')
    }

    void testEntityRefFromCpdScreen() {
        doTest(org.apache.ofbiz.idea.plugin.reference.common.EntityReference.class, 'Entito')
    }

    void testFileRefFromCpdScreen() {
        doTest(FileReference.class, 'footemplate', false)
    }

    void testCpdScreenRefFromExtScreen() {
        doTest(org.apache.ofbiz.idea.plugin.reference.xml.ScreenReference.class, 'MyScreenInCpd', false)
    }

    void testCpdFormRefFromExtScreen() {
        doTest(org.apache.ofbiz.idea.plugin.reference.xml.FormReference.class, 'MyFormInCpd', false)
    }

    //=====================================
    //              FORMS TESTS
    //=====================================

    void testCpdRequestMapRefFromCpdForm() {
        doTest(org.apache.ofbiz.idea.plugin.reference.xml.RequestMapReference.class, 'MyFooRequest')
    }

    void testCpdScreenRefFromCpdFieldForm() {
        doTest(org.apache.ofbiz.idea.plugin.reference.xml.ScreenReference.class, 'MyPitaScreen', false)
    }

    void testCpdFormRefFromCpdFieldForm() {
        doTest(org.apache.ofbiz.idea.plugin.reference.xml.FormReference.class, 'MyPitaForm')
    }

    void testCpdMenuRefFromCpdFieldForm() {
        doTest(MenuReference.class, 'MyPitaMenu')
    }

    //=====================================
    //         REQUEST MAP TESTS
    //=====================================

    void testCpdViewMapRefFromCpdRequestMap() {
        doTest(org.apache.ofbiz.idea.plugin.reference.xml.ViewMapReference.class, 'myFooResponseInCpd')
    }

    void testServiceRefFromCpdRequestMap() {
        doTest(org.apache.ofbiz.idea.plugin.reference.common.ServiceReference.class, 'DonateToQuadratureDuNet')
    }

    void testJavaEventRefFromCpdRequestMap() {
        doTest(JavaMethodReference.class, 'login')
    }

    //=====================================
    //         REQUEST MAP TESTS
    //=====================================

    void testCpdScreenRefFromCpdViewMap() {
        doTest(org.apache.ofbiz.idea.plugin.reference.xml.ScreenReference.class, 'MyFooScreenInCpd', false)
    }
}
