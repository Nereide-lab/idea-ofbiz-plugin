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

package fr.nereide.test.completion

class TestCompletionInJava extends BaseComplTestCase {

    String getFileType() { return 'java' }

    void testEntityCompletionInJavaFile() {
        List<String> expected = ['Yenefer', 'Roach']
        doTest(expected)
    }

    void testServiceCompletionInJavaFile() {
        List<String> expected = ['makeWitcher', 'makeHorse']
        doTest(expected)
    }

    void testEntityFieldCompletionInSimpleGetFieldFromGenericValueJavaClass() {
        List<String> expected = ['michael', 'maline']
        doTest(expected)
    }

    void testEntityFieldCompletionInGetFieldFromGenericValueJavaClassWithWhere() {
        List<String> expected = ['michael', 'maline']
        doTest(expected)
    }

    void testEntityFieldCompletionInGetFieldFromGVListInForLoop() {
        List<String> expected = ['michael', 'maline']
        doTest(expected)
    }

    void testEntityFieldCompletionInSimpleWhereInEntityQuery() {
        List<String> expected = ['michael', 'maline']
        doTest(expected)
    }

    void testEntityFieldCompletionInValueQueriedInTryCatch() {
        List<String> expected = ['michael', 'maline']
        doTest(expected)
    }

    void testEntityFieldCompletionInSimpleAddAliasInDynamicView() {
        List<String> expected = ['michael', 'maline']
        doTest(expected)
    }

    void testEntityFieldCompletionInFullAddAliasInDynamicView() {
        List<String> expected = ['michael', 'maline']
        doTest(expected)
    }

    void testEntityFieldCompletionInModelKeyMapFirstFieldAddViewLinkInDynamicView() {
        List<String> expected = ['michael', 'maline']
        doTest(expected)
    }
}
