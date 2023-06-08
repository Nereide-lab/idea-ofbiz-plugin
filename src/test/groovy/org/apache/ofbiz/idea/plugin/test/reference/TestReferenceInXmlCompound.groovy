/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.ofbiz.idea.plugin.test.reference

import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import org.apache.ofbiz.idea.plugin.reference.common.EntityReference
import org.apache.ofbiz.idea.plugin.reference.common.ServiceReference
import org.apache.ofbiz.idea.plugin.reference.xml.FormReference
import org.apache.ofbiz.idea.plugin.reference.xml.GridReference
import org.apache.ofbiz.idea.plugin.reference.xml.JavaMethodReference
import org.apache.ofbiz.idea.plugin.reference.xml.MenuReference
import org.apache.ofbiz.idea.plugin.reference.xml.RequestMapReference
import org.apache.ofbiz.idea.plugin.reference.xml.ScreenReference
import org.apache.ofbiz.idea.plugin.reference.xml.ViewMapReference

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
