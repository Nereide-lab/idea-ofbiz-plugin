/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License") you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package fr.nereide.reference.common

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet
import fr.nereide.project.ProjectServiceInterface
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

import java.util.regex.Matcher
import java.util.regex.Pattern

import static fr.nereide.project.utils.XmlUtils.getParentTag
import static fr.nereide.project.utils.XmlUtils.isPageReferenceFromController

class ComponentAwareFileReferenceSet extends FileReferenceSet {

    //regex retournant le nom du composant
    public static final Pattern COMPONENT_NAME_PATTERN = Pattern.compile("component://([^/]*)/")

    ComponentAwareFileReferenceSet(@NotNull String str, @NotNull PsiElement element,
                                   int startInElement, @Nullable PsiReferenceProvider provider,
                                   boolean isCaseSensitive, boolean endingSlashNotAllowed) {
        super(str, element, startInElement, provider, isCaseSensitive, endingSlashNotAllowed)
    }

    static ComponentAwareFileReferenceSet createSet(@NotNull PsiElement element, final boolean soft,
                                                    boolean endingSlashNotAllowed, final boolean urlEncoded) {
        // get only text
        ElementManipulator<PsiElement> manipulator = ElementManipulators.getManipulator(element)
        TextRange range = manipulator.getRangeInElement(element)
        int offset = range.getStartOffset()
        String text = range.substring(element.getText())

        return new ComponentAwareFileReferenceSet(text, element, offset, null, true, endingSlashNotAllowed)
    }

    /**
     * La fonction découpe le String de chemin de fichier et créée une référence pour chaque
     * partie de l'url. Ainsi, on peut naviguer au ctrl click vers les dossiers aussi.
     * @param str
     * @param startInElement
     * @return
     */
    protected List<FileReference> reparse(String str, int startInElement) {
        Matcher componentMatcher = COMPONENT_NAME_PATTERN.matcher(str)

        if (componentMatcher.find() && componentMatcher.groupCount() != 0) {
            String component = componentMatcher.group(1)
            int i = 0 // Position index in path reference string
            FileReference componentBaseReference = createComponentBaseDirReference(this,
                    componentMatcher.start(1), componentMatcher.end(1), i++, component)

            List<FileReference> referencesList = []
            if (componentBaseReference != null) {
                referencesList << componentBaseReference
            }

            List<String> pathPieces = str.split("\\s*/\\s*")
                    .findAll { String fileElement -> fileElement && !fileElement.equalsIgnoreCase("component:") }
                    .collect { it.contains('#') ? it.substring(0, it.lastIndexOf('#')) : it }
                    .toList()

            while (i < pathPieces.size()) {
                String pathPiece = pathPieces[i]
                TextRange pathPosition = pathPiece.equals(component) ?
                        getRangeForSecondOccurrenceOfString(str, pathPiece) :
                        getRangeForUniqueString(str, pathPiece)
                FileReference reference = this.createFileReference(pathPosition, i++, pathPiece)
                if (reference != null) {
                    referencesList.add(reference)
                }
            }
            return referencesList
        } else {
            return super.reparse(str, startInElement)
        }
    }

    private static TextRange getRangeForUniqueString(String str, String pathPiece) {
        int start = str.indexOf(pathPiece) + 1
        int end = str.indexOf(pathPiece) + pathPiece.length() + 1
        return new TextRange(start, end)
    }

    private static TextRange getRangeForSecondOccurrenceOfString(String str, String pathPiece) {
        int start = str.indexOf(pathPiece, str.indexOf(pathPiece) + 1) + 1
        int end = str.indexOf(pathPiece, str.indexOf(pathPiece) + 1) + pathPiece.length() + 1
        return new TextRange(start, end)
    }

    protected static FileReference createComponentBaseDirReference(FileReferenceSet set, int textRangeStart, int textRangeEnd,
                                                                   int index, String component) {
        return new FileReference(set, new TextRange(textRangeStart, textRangeEnd), index, component) {
            @NotNull
            ResolveResult[] multiResolve(boolean incompleteCode) {
                ProjectServiceInterface structureService = set.getElement().getProject().getService(ProjectServiceInterface.class)
                PsiDirectory vf = structureService.getComponentDir(component)
                if (vf != null) {
                    ResolveResult[] result = new PsiElementResolveResult(vf)
                    return result
                } else {
                    return ResolveResult.EMPTY_ARRAY
                }
            }
        }
    }
}
