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

package fr.nereide.test.inspection

import fr.nereide.inspection.CacheOnNeverCacheEntityInspection
import fr.nereide.inspection.InspectionBundle

class JavaInspectionTest extends BaseInspectionTest {

    @Override
    String getLang() {
        return 'java'
    }

    void testCacheOnNeverCacheEntity() {
        String intention = InspectionBundle.message('inspection.entity.cache.on.never.cache.use.quickfix')
        myFixture.enableInspections(new CacheOnNeverCacheEntityInspection())
        doTest(intention)
    }

    void testCacheOnNeverCacheEntityWithTrueParameter() {
        String intention = InspectionBundle.message('inspection.entity.cache.on.never.cache.use.quickfix')
        myFixture.enableInspections(new CacheOnNeverCacheEntityInspection())
        doTest(intention)
    }
}
