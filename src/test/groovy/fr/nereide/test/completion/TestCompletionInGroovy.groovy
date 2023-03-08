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

    void testEntityCompletionInGroovyFile() {
        List<String> expected = ['Yenefer', 'Roach']
        doTest(expected)
    }

    void testServiceCompletionInGroovyFile() {
        List<String> expected = ['makeWitcher', 'makeHorse']
        doTest(expected)
    }

    void testEntityFieldCompletionInGroovyFileWithSimpleEntity() {
        List<String> expected = ['michael']
        doTest(expected)
    }

    void testEntityFieldCompletionInGroovyFileWithViewNoNested() {
        List<String> expected = ['jo', 'gabriel', 'michael'],
                     notExpected = ['maline']
        doTest(expected, notExpected)
    }

    void testEntityFieldCompletionInGroovyFileWithViewAndNested() {
        List<String> expected = ['michael', 'jo', 'gabriel', 'david']
        doTest(expected)
    }

    void testEntityFieldCompletionInGroovyFileWithRecursiveView() {
        List<String> expected = ['david']
        doTest(expected)
    }

    void testEntityFieldCompletionInGroovyFileWithViewAndExclude() {
        List<String> expected = ['johnny', 'johnkreese'],
                     notExpected = ['daniel']
        doTest(expected, notExpected)
    }

    void testEntityFieldCompletionInGroovyFileWithViewAndExcludeViewField() {
        List<String> expected = ['johnny'],
                     notExpected = ['daniel', 'johnkreese']
        doTest(expected, notExpected)
    }

    void testEntityFieldCompletionInGroovyFileWithSimpleViewAndPrefix() {
        List<String> expected = ['bigshovelreese', 'bigshovelfrancis', 'bigshovellois', 'reese', 'francis', 'lois']
        doTest(expected)
    }

    void testEntityFieldCompletionInGroovyFileWithComplexViewAndPrefix() {
        List<String> expected = ['geniusbigshovelreese', 'geniusbigshovelfrancis', 'geniusbigshovellois',
                                 'geniusreese', 'geniusfrancis', 'geniuslois']
        doTest(expected)
    }

    void testEntityFieldCompletionInGroovyFileInLoopOfListWithExplicitTypeAndForeachLoop() {
        List<String> expected = ['sauron']
        doTest(expected)
    }

    void testEntityFieldCompletionInGroovyFileInGVListStreamWithExplicitType() {
        List<String> expected = ['sauron']
        doTest(expected)
    }

    void testEntityFieldCompletionInGroovyFileInForLoop() {
        List<String> expected = ['sauron']
        doTest(expected)
    }
}