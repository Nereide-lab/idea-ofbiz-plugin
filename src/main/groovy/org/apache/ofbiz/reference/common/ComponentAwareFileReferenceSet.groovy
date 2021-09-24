package org.apache.ofbiz.reference.common

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet
import org.apache.ofbiz.project.ProjectServiceInterface
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

import java.util.regex.Matcher
import java.util.regex.Pattern

class ComponentAwareFileReferenceSet extends FileReferenceSet {

    //regex retournant le nom du composant
    private static final Pattern COMPONENT_NAME_PATTERN = Pattern.compile("component://([^/]*)/")

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

        return new ComponentAwareFileReferenceSet(text, element, offset, null, true, endingSlashNotAllowed) {
            protected boolean isUrlEncoded() {
                return urlEncoded
            }

            protected boolean isSoft() {
                return soft
            }
        }
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
            List<FileReference> referencesList = new ArrayList<>()
            String component = componentMatcher.group(1)
            int i = 0

            FileReference componentBaseReference =
                    createComponentBaseDirReference(this, componentMatcher.start(1), componentMatcher.end(1), i++, component)
            if (componentBaseReference != null) {
                referencesList.add(componentBaseReference)
            }
            List<String> pathPieces = Arrays.asList(str.split("\\s*/\\s*"))
            for (String pathPiece : pathPieces) {
                if (pathPiece != "" && !pathPiece.contains("component:") && pathPiece != component) {
                    TextRange pathPosition = new TextRange(str.indexOf(pathPiece) + 1,
                            str.indexOf(pathPiece) + pathPiece.length() + 1)
                    FileReference reference = this.createFileReference(pathPosition, i++, pathPiece)
                    if (reference != null) {
                        referencesList.add(reference)
                    }
                }
            }
            return referencesList
        } else {
            return super.reparse(str, startInElement)
        }
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
