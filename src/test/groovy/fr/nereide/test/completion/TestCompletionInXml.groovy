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

class TestCompletionInXml extends BaseComplTestCase {

    String getFileType() { return 'xml' }

    void testEntityCompletionInXmlFile() {
        List<String> expected = ['Yenefer', 'Roach']
        doTest(expected)
    }

    void testServiceCompletionInXmlFile() {
        List<String> expected = ['makeWitcher', 'makeHorse']
        doTest(expected)
    }

    void testEntityFieldCompletionInViewInAliasInName() {
        List<String> expected = ['mobId', 'colorId']
        doTest(expected)
    }

    void testEntityFieldCompletionInViewInAliasInField() {
        List<String> expected = ['genderId', 'name']
        doTest(expected)
    }

    void testNoEntityFieldCompletionInViewInAliasInNameWithField() {
        doTest(null)
    }

    void testEntityFieldCompletionInViewInKeyMapInFieldName() {
        List<String> expected = ['rachel', 'ross']
        doTest(expected)
    }

    void testEntityFieldCompletionInViewInKeyMapInRelFieldName() {
        List<String> expected = ['monica', 'ross']
        List<String> notExpected = ['rachel']
        doTest(expected, notExpected)
    }

    void testEntityNameCompletionInEntityEngineFile() {
        List<String> expected = ['Yenefer', 'Roach', 'DunderMifflin', 'Sabre', 'DunderCorporate', 'CobraKai',
                                 'Malcolm', 'Mordor', 'RossCouple', 'RossAndSister']
        List<String> notExpected = ['WholePaperCompany', 'RecursiveView', 'CobraKaiNeverDie', 'Miyagido', 'InTheMiddle',
                                    'LifeIsUnfair', 'PaperCompany']
        doTest(expected, notExpected)
    }

    void testEntityFieldCompletionInEntityEngineFile() {
        List<String> expected = ['michael', 'maline']
        doTest(expected)
    }

    void testScreenNameCompletionInSameFile() {
        List<String> expected = ['ScottScreen']
        doTest(expected)
    }

    void testScreenNameCompletionInSameFileWithOtherScreensInOtherFiles() {
        List<String> expected = ['PilgrimScreen']
        List<String> notExpected = ['NopeScreen']
        doTest(expected, notExpected)
    }
}
