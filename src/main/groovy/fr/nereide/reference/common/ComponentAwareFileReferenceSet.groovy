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
package fr.nereide.reference.common

import static fr.nereide.project.pattern.OfbizPluginConstants.FILE_AND_ELEMENT_SEPARATOR

import com.intellij.openapi.util.TextRange
import com.intellij.psi.ElementManipulator
import com.intellij.psi.ElementManipulators
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.ResolveResult
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet
import fr.nereide.project.OfbizProjectHelper
import org.jetbrains.annotations.NotNull

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Enables the plugin to parse locations of the OFBiz format like
 * location="component://forest-component/widget/File.xml"/>
 */
class ComponentAwareFileReferenceSet extends FileReferenceSet {

    public static final Pattern COMPONENT_NAME_PATTERN = Pattern.compile('component://([^/]*)/')

    ComponentAwareFileReferenceSet(@NotNull String str, @NotNull PsiElement element, int startInElement,
                                   boolean endingSlashNotAllowed) {
        super(str, element, startInElement, null, true, endingSlashNotAllowed)
    }

    static ComponentAwareFileReferenceSet make(@NotNull PsiElement element, boolean endingSlashNotAllowed) {
        // get only text
        ElementManipulator<PsiElement> manipulator = ElementManipulators.getManipulator(element)
        TextRange range = manipulator.getRangeInElement(element)
        int offset = range.startOffset
        String text = range.substring(element.text)
        return new ComponentAwareFileReferenceSet(text, element, offset, endingSlashNotAllowed)
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

    protected static FileReference makeComponentBaseDirReference(FileReferenceSet set, int textRangeStart,
                                                                 int textRangeEnd, int index, String component) {
        return new FileReference(set, new TextRange(textRangeStart, textRangeEnd), index, component) {

            @NotNull
            ResolveResult[] multiResolve(boolean incompleteCode) { // codenarc-disable UnusedMethodParameter
                OfbizProjectHelper ph = OfbizProjectHelper.getInstance(set.element.project)
                PsiDirectory vf = ph.getComponentDir(component)
                if (vf != null) {
                    ResolveResult[] result = new PsiElementResolveResult(vf)
                    return result
                }
                return ResolveResult.EMPTY_ARRAY
            }

        }
    }

    /**
     * La fonction découpe le String de chemin de fichier et créée une référence pour chaque
     * partie de l'url. Ainsi, on peut naviguer au ctrl click vers les dossiers aussi.
     * @param str the string to reparse
     * @param startInElement element offset if any
     * @return an organized list of folders and files toward the reference files
     */
    protected List<FileReference> reparse(String str, int startInElement) {
        Matcher componentMatcher = COMPONENT_NAME_PATTERN.matcher(str)

        if (componentMatcher.find() && componentMatcher.groupCount() != 0) {
            String component = componentMatcher.group(1)
            int i = 0 // Position index in path reference string
            FileReference componentBaseReference = makeComponentBaseDirReference(this,
                    componentMatcher.start(1), componentMatcher.end(1), i++, component)

            List<FileReference> referencesList = []
            if (componentBaseReference != null) {
                referencesList << componentBaseReference
            }

            List<String> pathPieces = str.split('\\s*/\\s*')
                    .findAll { fileElement -> fileElement && !fileElement.equalsIgnoreCase('component:') }
                    .collect { pathPiece ->
                        pathPiece.contains(FILE_AND_ELEMENT_SEPARATOR) ?
                                pathPiece.substring(0, pathPiece.lastIndexOf(FILE_AND_ELEMENT_SEPARATOR)) :
                                pathPiece
                    }.toList()

            while (i < pathPieces.size()) {
                String pathPiece = pathPieces[i]
                TextRange pathPosition = pathPiece == component ?
                        getRangeForSecondOccurrenceOfString(str, pathPiece) :
                        getRangeForUniqueString(str, pathPiece)
                FileReference reference = this.createFileReference(pathPosition, i++, pathPiece)
                if (reference != null) {
                    referencesList.add(reference)
                }
            }
            return referencesList
        }
        return super.reparse(str, startInElement)
    }

}
