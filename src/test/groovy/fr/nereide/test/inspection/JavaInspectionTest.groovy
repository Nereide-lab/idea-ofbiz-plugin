/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * 'License') you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * 'AS IS' BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package fr.nereide.test.inspection

import static fr.nereide.inspection.InspectionBundle.message

import fr.nereide.inspection.java.CacheOnNeverCacheEntityJavaInspection
import fr.nereide.inspection.java.DuplicatedEntityJavaInspection
import fr.nereide.inspection.java.DuplicatedServiceJavaInspection
import fr.nereide.inspection.java.EntityNotFoundInJavaInspection
import fr.nereide.inspection.java.ServiceNotFoundInJavaInspection

/**
 * Inspection tests in java
 */
class JavaInspectionTest extends BaseInspectionTest {

    /* codenarc-disable JUnitTestMethodWithoutAssert */

    void testCacheOnNeverCacheEntity() {
        doNeverCacheTest(true)
    }

    void testCacheOnNeverCacheEntityWithTrueParameter() {
        doNeverCacheTest(true)
    }

    void testCacheOnNeverCacheEntityWithFalseParameter() {
        doNeverCacheTest(false)
    }

    void testCacheOnNeverCacheView() {
        doNeverCacheTest(true)
    }

    void testCacheOnViewIncludingNeverCacheEntity() {
        doNeverCacheTest(false)
    }

    void testDuplicatedServiceInspection() {
        doHighlightTest(true, message('inspection.service.duplicate.display.descriptor'),
                new DuplicatedServiceJavaInspection())
    }

    void testDuplicatedEntityInspection() {
        doHighlightTest(true, message('inspection.entity.duplicate.display.descriptor'),
                new DuplicatedEntityJavaInspection())
    }

    void testServiceNotFoundInspection() {
        doHighlightTest(true, message('inspection.service.not.found.display.descriptor'),
                new ServiceNotFoundInJavaInspection())
    }

    void testServiceNotFoundInspectionSafety() {
        doHighlightTest(false, message('inspection.service.not.found.display.descriptor'),
                new ServiceNotFoundInJavaInspection())
    }

    void testEntityNotFoundInspection() {
        doHighlightTest(true, message('inspection.entity.not.found.display.descriptor'),
                new EntityNotFoundInJavaInspection())
    }

    void testEntityNotFoundInspectionSafety() {
        doHighlightTest(false, message('inspection.entity.not.found.display.descriptor'),
                new EntityNotFoundInJavaInspection())
    }
    /* codenarc-enable JUnitTestMethodWithoutAssert */

    @Override
    protected String getLang() {
        return 'java'
    }

    protected void doNeverCacheTest(boolean mustFind) {
        doNeverCacheTest(mustFind, new CacheOnNeverCacheEntityJavaInspection())
    }

}
