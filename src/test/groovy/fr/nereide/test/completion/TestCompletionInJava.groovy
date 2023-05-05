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

    /**
     * {@code EntityQuery.from("<caret>"); }
     */
    void testEntityCompletionInJavaFile() {
        List<String> expected = ['Yenefer', 'Roach']
        doTest(expected)
    }

    /**
     * {@code dispatcher.runSync('<caret>'); }
     */
    void testServiceCompletionInJavaFile() {
        List<String> expected = ['makeWitcher', 'makeHorse']
        doTest(expected)
    }

    //**********************************************
    //                 ENTITY FIELDS              //
    //**********************************************

    /**
     * <pre>
     * {@code basic query
     * String foo = testData.get("<caret>");
     *} </pre>
     */
    void testEntityFieldCompletionInSimpleGetFieldFromGenericValueJavaClass() {
        List<String> expected = ['michael', 'maline']
        doTest(expected)
    }

    /**
     * <pre>
     * {@code query.where..
     * String foo = testData.get("<caret>")
     *} </pre>
     */
    void testEntityFieldCompletionInGetFieldFromGenericValueJavaClassWithWhere() {
        List<String> expected = ['michael', 'maline']
        doTest(expected)
    }

    /**
     * <pre> {@code for (GenericValue testData : testDataList) {
     *      String foo = testData.get("<caret>");
     *  }}
     */
    void testEntityFieldCompletionInGetFieldFromGVListInForLoop() {
        List<String> expected = ['michael', 'maline']
        doTest(expected)
    }

    /**
     * {@code EntityQuery.use(delegator).from("DunderMifflin").where("<caret>", "bar").queryFirst(); }
     */
    void testEntityFieldCompletionInSimpleWhereInEntityQuery() {
        List<String> expected = ['michael', 'maline']
        doTest(expected)
    }

    /**
     * <pre> {@code GenericValue testData;
     *  try {
     *      testData = //..
     *  } catch {//.. }
     *  String foo = testData.get("<caret>");}
     *  </pre>
     */
    void testEntityFieldCompletionInValueQueriedInTryCatch() {
        List<String> expected = ['michael', 'maline']
        doTest(expected)
    }

    /**
     * {@code myDve.addAlias("OI", "<caret>");}
     */
    void testEntityFieldCompletionInSimpleAddAliasInDynamicView() {
        List<String> expected = ['michael', 'maline']
        doTest(expected)
    }

    /**
     * {@code myDve.addAlias("OI", "name", "<caret>", null, null, null, null);}
     */
    void testEntityFieldCompletionInFullAddAliasInDynamicView() {
        List<String> expected = ['michael', 'maline']
        doTest(expected)
    }

    /**
     * {@code myDve.addViewLink("DM", "OP", Boolean.FALSE, ModelKeyMap.makeKeyMapList("<caret>")); }
     */
    void testEntityFieldCompletionInModelKeyMapFirstFieldAddViewLinkInDynamicView() {
        List<String> expected = ['michael', 'maline']
        doTest(expected)
    }

    /**
     * {@code myDve.addViewLink("SB", "DM", Boolean.FALSE, ModelKeyMap.makeKeyMapList("gabriel", "<caret>")); }
     */
    void testEntityFieldCompletionInModelKeyMapSecondFieldAddViewLinkInDynamicView() {
        List<String> notExpected = ['gabriel', 'jo']
        List<String> expected = ['michael', 'maline']
        doTest(expected, notExpected)
    }
}
