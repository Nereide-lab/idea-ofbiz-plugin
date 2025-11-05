package fr.nereide.test.inspection

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiElementFilter
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlTag
import fr.nereide.test.BaseOfbizPluginTestCase
import org.jetbrains.annotations.NotNull

/**
 * Base class for inspection tests
 */
abstract class BaseInspectionTest extends BaseOfbizPluginTestCase {

    abstract String getLang()

    @Override
    protected void setUp() {
        super.setUp()
        myFixture.copyDirectoryToProject('assets', '')
    }

    @Override
    protected String getTestDataPath() {
        return 'src/test/resources/testData/inspection'
    }

    protected void doInspectionThenQuickFixWithXmlElementCreate(boolean mustFind, String intention, String desc,
                                                                String expectedFileLocation, String elName,
                                                                String elType) {
        myFixture.configureByFile(testFile)
        doHighlightTest(mustFind, desc)
        if (!mustFind) return
        findAndLaunchAction(intention)
        PsiFile fileToLookIn = expectedFileLocation ? getExpectedFile(expectedFileLocation) : myFixture.file
        List<XmlTag> tags = PsiTreeUtil.collectElements(fileToLookIn, tagFilter)
        assert tags.any { XmlTag tag ->
            tag.getAttribute('name')?.value == elName && tag.name == elType
        }
    }

    protected void doInspectionThenQuickFixTestWithFileEdit(boolean mustFind, String intention, String desc) {
        myFixture.configureByFile(testFile)
        doHighlightTest(mustFind, desc)
        if (!mustFind) return
        findAndLaunchAction(intention)
        myFixture.checkResultByFile(expectedFilePath, true)
    }

    protected void doFileInspectionTestWithFileCreation(boolean mustFind, String intention, String desc,
                                                        String expectedFileLocation) {
        myFixture.configureByFile(testFile)
        doHighlightTest(mustFind, desc)
        if (!mustFind) return
        findAndLaunchAction(intention)
        assertNotNull myFixture.tempDirFixture.getFile(expectedFileLocation)
    }

    protected void doHighlightTest(boolean mustFind, String desc) {
        List<HighlightInfo> highlightInfos = myFixture.doHighlighting()
        List<String> highlightDescs = highlightInfos.collect { it.description }
        if (!mustFind) {
            assert !highlightDescs.contains(desc)
        } else {
            assertFalse highlightInfos.empty
            assert highlightDescs.contains(desc)
        }
    }

    private void findAndLaunchAction(String intention) {
        final IntentionAction action = myFixture.findSingleIntention(intention)
        assertNotNull action
        myFixture.launchAction(action)
    }

    void doHighlightTest(boolean shouldFind, String message, LocalInspectionTool inspection) {
        myFixture.enableInspections(inspection)
        myFixture.configureByFile(testFile)
        doHighlightTest(shouldFind, message)
    }

    //#####################################
    // UTILS
    //#####################################
    String getExpectedFilePath() {
        return "${lang}/${getTestName(false)}.after.${lang}"
    }

    String getTestFile() {
        return "${lang}/${getTestName(false)}.${lang}"
    }

    protected void doMove() {
        String file = "xml/${this.getTestName(false)}.xml"
        myFixture.moveFile(file, 'zelda/widget')
    }

    private PsiFile getExpectedFile(String expectedFileLocation) {
        VirtualFile virtualFile = myFixture.tempDirFixture.getFile(expectedFileLocation)
        PsiManager psiMan = PsiManager.getInstance(myFixture.project)
        assertNotNull virtualFile
        return psiMan.findFile(virtualFile)
    }

    private static PsiElementFilter getTagFilter() {
        new PsiElementFilter() {

            @Override
            boolean isAccepted(@NotNull PsiElement psiElement) {
                boolean isTag = psiElement instanceof XmlTag
                if (!isTag) return false
                boolean hasRelevantAttr = (psiElement as XmlTag).getAttribute('name')
                return isTag && hasRelevantAttr
            }

        }
    }

}
