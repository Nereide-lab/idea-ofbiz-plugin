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

    void testEntityCompletionInGroovyFile() {
        List<String> lookupElementStrings = configureByFileAndGetLookupsElements('groovy/EntityCompletionInGroovyFile.groovy')
        assertContainsElements(lookupElementStrings, 'Yenefer', 'Roach')
    }

    void testServiceCompletionInGroovyFile() {
        List<String> lookupElementStrings = configureByFileAndGetLookupsElements('groovy/ServiceCompletionInGroovyFile.groovy')
        assertContainsElements(lookupElementStrings, 'makeWitcher', 'makeHorse')
    }

    void testEntityFieldCompletionInGroovyFileWithSimpleEntity() {
        List<String> lookupElementStrings = configureByFileAndGetLookupsElements('groovy/EntityFieldCompletionInGroovyFileWithSimpleEntity.groovy')
        assertContainsElements(lookupElementStrings, 'michael')
    }

    void testEntityFieldCompletionInGroovyFileWithViewByAlias() {
        List<String> lookupElementStrings = configureByFileAndGetLookupsElements('groovy/EntityFieldCompletionInGroovyFileWithViewNoNested.groovy')
        assertContainsElements(lookupElementStrings, 'michael')
        assertDoesntContain(lookupElementStrings, 'maline')
    }

    void testEntityFieldCompletionInGroovyFileWithViewByAliasAll() {
        List<String> lookupElementStrings = configureByFileAndGetLookupsElements('groovy/EntityFieldCompletionInGroovyFileWithViewNoNested.groovy')
        assertContainsElements(lookupElementStrings, 'jo', 'gabriel')
    }

    void testEntityFieldCompletionInGroovyFileWithSimpleView() {
        List<String> lookupElementStrings = configureByFileAndGetLookupsElements('groovy/EntityFieldCompletionInGroovyFileWithViewNoNested.groovy')
        assertContainsElements(lookupElementStrings, 'jo', 'gabriel', 'michael')
    }

    void testEntityFieldCompletionInGroovyFileWithViewAndNested() {
        List<String> lookupElementStrings = configureByFileAndGetLookupsElements('groovy/EntityFieldCompletionInGroovyFileWithViewAndNested.groovy')
        assertContainsElements(lookupElementStrings, 'michael', 'jo', 'gabriel', 'david')
    }

    void testEntityFieldCompletionInGroovyFileWithRecursiveView() {
        List<String> lookupElementStrings = configureByFileAndGetLookupsElements('groovy/EntityFieldCompletionInGroovyFileWithRecursiveView.groovy')
        assertContainsElements(lookupElementStrings, 'david')
    }

    void testEntityFieldCompletionInGroovyFileWithViewAndExclude() {
        List<String> lookupElementStrings = configureByFileAndGetLookupsElements('groovy/EntityFieldCompletionInGroovyFileWithViewAndExclude.groovy')
        assertContainsElements(lookupElementStrings, 'johnny', 'johnkreese')
        assertDoesntContain(lookupElementStrings, 'daniel')
    }

    void testEntityFieldCompletionInGroovyFileWithViewAndExcludeViewField() {
        List<String> lookupElementStrings = configureByFileAndGetLookupsElements('groovy/EntityFieldCompletionInGroovyFileWithViewAndExcludeViewField.groovy')
        assertContainsElements(lookupElementStrings, 'johnny')
        assertDoesntContain(lookupElementStrings, 'daniel', 'johnkreese')
    }

    void testEntityFieldCompletionInGroovyFileWithSimpleViewAndPrefix() {
        List<String> lookupElementStrings = configureByFileAndGetLookupsElements(
                'groovy/EntityFieldCompletionInGroovyFileWithSimpleViewAndPrefix.groovy')
        assertContainsElements(lookupElementStrings, 'bigshovelreese', 'bigshovelfrancis', 'bigshovellois',
                'reese', 'francis', 'lois')
    }

    void testEntityFieldCompletionInGroovyFileWithComplexViewAndPrefix() {
        List<String> lookupElementStrings = configureByFileAndGetLookupsElements(
                'groovy/EntityFieldCompletionInGroovyFileWithComplexViewAndPrefix.groovy')
        assertContainsElements(lookupElementStrings, 'geniusbigshovelreese', 'geniusbigshovelfrancis',
                'geniusbigshovellois', 'geniusreese', 'geniusfrancis', 'geniuslois')
    }

    void testEntityFieldCompletionInGroovyFileInLoopOfListWithExplicitTypeAndForeachLoop() {
        List<String> lookupElementStrings = configureByFileAndGetLookupsElements(
                'groovy/EntityFieldCompletionInGroovyFileInLoopOfListWithExplicitTypeAndForeachLoop.groovy')
        assertContainsElements(lookupElementStrings, 'sauron')
    }

    void testEntityFieldCompletionInGroovyFileInGVListStreamWithExplicitType() {
        List<String> lookupElementStrings = configureByFileAndGetLookupsElements(
                'groovy/EntityFieldCompletionInGroovyFileInGVListStreamWithExplicitType.groovy')
        assertContainsElements(lookupElementStrings, 'sauron')
    }

    void testEntityFieldCompletionInGroovyFileInForLoop() {
        List<String> lookupElementStrings = configureByFileAndGetLookupsElements(
                'groovy/EntityFieldCompletionInGroovyFileInForLoop.groovy')
        assertContainsElements(lookupElementStrings, 'sauron')
    }
}