/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package fr.nereide.test.reference

import com.intellij.psi.PsiReference
import fr.nereide.reference.groovy.EntityGroovyReference
import fr.nereide.reference.groovy.ServiceGroovyReference

class TestReferenceInGroovy extends GenericRefTestCase {

    void testGroovyEntityReferenceWithFindMethod() {
        PsiReference ref = setupFixtureForTestAndGetRefForGroovy('groovy/GroovyEntityReferenceWithFindMethod.groovy')
        assertTrue ref instanceof EntityGroovyReference
        EntityGroovyReference entityRef = (EntityGroovyReference) ref
        assertEquals 'Lobster', entityRef.getValue() as String
        assertNotNull ref.resolve()
    }

    void testGroovyViewEntityReferenceWithFindMethod() {
        PsiReference ref = setupFixtureForTestAndGetRefForGroovy('groovy/GroovyViewEntityReferenceWithFindMethod.groovy')
        assertTrue ref instanceof EntityGroovyReference
        EntityGroovyReference entityRef = (EntityGroovyReference) ref
        assertEquals 'Zaun', entityRef.getValue() as String
        assertNotNull ref.resolve()
    }

    void testGroovyEntityReferenceWithFromMethod() {
        PsiReference ref = setupFixtureForTestAndGetRefForGroovy('groovy/GroovyEntityReferenceWithFromMethod.groovy')
        assertTrue ref instanceof EntityGroovyReference
        EntityGroovyReference entityRef = (EntityGroovyReference) ref
        assertEquals 'PickleRick', entityRef.getValue() as String
        assertNotNull ref.resolve()
    }

    void testGroovyServiceReferenceWithRunCall() {
        PsiReference ref = setupFixtureForTestAndGetRefForGroovy('groovy/GroovyServiceReferenceWithRunCall.groovy')
        assertTrue ref instanceof ServiceGroovyReference
        ServiceGroovyReference serviceRef = (ServiceGroovyReference) ref
        assertEquals 'pivot', serviceRef.getValue() as String
        assertNotNull ref.resolve()
    }
}
