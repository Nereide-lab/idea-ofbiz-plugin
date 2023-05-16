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


class TestCompletionInGroovy extends BaseComplTestCase {

    String getFileType() { return 'groovy' }

    /**
     * {@code def myVar = EntityQuery.use(delegator).from('<caret>').queryFirst()}
     */
    void testEntityCompletionInGroovyFile() {
        List<String> expected = ['Yenefer', 'Roach']
        doTest(expected)
    }

    /**
     * {@code var result = dispatcher.runSync('<caret>', null)}
     */
    void testServiceCompletionInGroovyFile() {
        List<String> expected = ['makeWitcher', 'makeHorse']
        doTest(expected)
    }

    /**
     * <pre>
     * {@code org.apache.ofbiz.entity.GenericValue paper = from('DunderMifflin')
     *      def foo = paper.<caret> }</pre>
     */
    void testEntityFieldCompletionInGroovyFileWithSimpleEntity() {
        List<String> expected = ['michael']
        doTest(expected)
    }

    //**********************************************
    //                 ENTITY FIELDS              //
    //**********************************************

    /**
     * <pre>
     * {@code List<org.apache.ofbiz.entity.GenericValue> rings = from('Mordor')
     *      rings.forEach { org.apache.ofbiz.entity.GenericValue it -> it.<caret> }
     *} </pre>
     */
    void testEntityFieldCompletionInGroovyFileInLoopOfListWithExplicitTypeAndForeachLoop() {
        List<String> expected = ['sauron']
        doTest(expected)
    }

    /**
     * <pre>
     * {@code List<org.apache.ofbiz.entity.GenericValue> rings = from('Mordor')
     * rings.stream().filter({ GenericValue it -> it.<caret> })
     *} </pre>
     */
    void testEntityFieldCompletionInGroovyFileInGVListStreamWithExplicitType() {
        List<String> expected = ['sauron']
        doTest(expected)
    }

    /**
     * <pre>
     * {@code List<GenericValue> rings = from('Mordor')
     * for (GenericValue ring : rings) { ring.<caret> }
     *} </pre>
     */
    void testEntityFieldCompletionInGroovyFileInForLoop() {
        List<String> expected = ['sauron']
        doTest(expected)
    }

    /**
     * <pre>
     * {@code GenericValue myVal = EntityQuery.use(delegator).from('RossAndSister').where('foo', 'bar').queryFirst()
     * myVal.get('<caret>')
     *} </pre>
     */
    void testEntityFieldCompletionInSimpleGetFieldFromGenericValue() {
        List<String> expected = ['ross', 'monica']
        doTest(expected)
    }


    /**
     * Compiled
     * <pre>
     * {@code List<GenericValue> myVals = EntityQuery.use(delegator).from('RossAndSister').where('foo', 'bar').queryList()
     * for (GenericValue myVal : myVals) { myVal.get('<caret>') }
     *} </pre>
     */
    void testEntityFieldCompletionInGetFieldFromGVListInForLoop() {
        List<String> expected = ['ross', 'monica']
        doTest(expected)
    }

    /**
     * Script
     * <pre>
     * {@code List<GenericValue> myVals = EntityQuery.use(delegator).from('RossAndSister').where('foo', 'bar').queryList()
     * for (GenericValue myVal : myVals) { myVal.get('<caret>') }
     *} </pre>
     */
    void testEntityFieldCompletionInGetFieldFromGVListInForLoopInScript() {
        List<String> expected = ['ross', 'monica']
        doTest(expected)
    }

    /**
     * Compiled
     * {@code GenericValue myVal = EntityQuery.use(delegator).from('RossAndSister').where('<caret>').queryList() }
     */
    void testEntityFieldCompletionInSimpleWhereInEntityQuery() {
        List<String> expected = ['ross', 'monica']
        doTest(expected)
    }

    /**
     * Script
     * {@code GenericValue lookedValue = from('RossAndSister').where('<caret>').queryOne()}
     */
    void testEntityFieldCompletionInSimpleWhereInEntityQueryInScript() {
        List<String> expected = ['ross', 'monica']
        doTest(expected)
    }

    /**
     * Compiled
     * {@code
     * GenericValue myVal = null
     * try { //query
     * } catch (Exception ignored) { }
     * myVal.get('<caret>')
     *}
     */
    void testEntityFieldCompletionInValueQueriedInTryCatch() {
        List<String> expected = ['ross', 'monica']
        doTest(expected)
    }

    /**
     * Script
     * {@code
     * GenericValue myVal = null
     * try { //query
     * } catch (Exception ignored) { }
     * myVal.get('<caret>')
     *}
     */
    void testEntityFieldCompletionInValueQueriedInTryCatchInScript() {
        List<String> expected = ['ross', 'monica']
        doTest(expected)
    }

//
//    /**
//     */
//    void testEntityFieldCompletionInValueQueriedInTryCatchInScriptGroovySyntax() {
//        assert true
//    }
//    /**
//     */
//    void testEntityFieldCompletionInSimpleAddAliasInDynamicView() {
//        assert true
//    }
//
//    /**
//     */
//    void testEntityFieldCompletionInFullAddAliasInDynamicView() {
//        assert true
//    }
//
//    /**
//     */
//    void testEntityFieldCompletionInModelKeyMapFirstFieldAddViewLinkInDynamicView() {
//        assert true
//    }
//
//    /**
//     */
//    void testEntityFieldCompletionInModelKeyMapSecondFieldAddViewLinkInDynamicView() {
//        assert true
//    }
}