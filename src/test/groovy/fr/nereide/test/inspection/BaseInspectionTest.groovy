package fr.nereide.test.inspection

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiElementFilter
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlTag
import fr.nereide.test.BaseOfbizPluginTestCase
import org.jetbrains.annotations.NotNull

abstract class BaseInspectionTest extends BaseOfbizPluginTestCase {

    abstract String getLang()

    String getExpectedFilePath() {
        return "${getLang()}/${getTestName(false)}.after.${getLang()}"
    }

    String getTestFile() {
        return "${getLang()}/${getTestName(false)}.${getLang()}"
    }

    @Override
    protected void setUp() {
        super.setUp()
        myFixture.copyDirectoryToProject('assets', '')
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/testData/inspection"
    }

    protected void doFileFixTest(String intention) {
        doFileFixTest(intention, null, true)
    }

    protected void doFileFixTest(String intention, String desc, boolean mustFind) {
        myFixture.configureByFile(testFile)
        List<HighlightInfo> highlightInfos = myFixture.doHighlighting()
        if (mustFind) {
            assertFalse(highlightInfos.isEmpty())
            final IntentionAction action = myFixture.findSingleIntention(intention)
            assertNotNull(action)
            myFixture.launchAction(action)
            myFixture.checkResultByFile(expectedFilePath, true)
        } else {
            List highlightDescs = highlightInfos.collect { it.description }
            assertFalse highlightDescs.contains(desc)
        }
    }

    protected void doFileInspectionTest(String intention, String expectedFileLocation, boolean mustFind) {
        myFixture.configureByFile(testFile)
        List<HighlightInfo> highlightInfos = myFixture.doHighlighting()
        if (mustFind) {
            assertFalse highlightInfos.isEmpty()
            final IntentionAction action = myFixture.findSingleIntention(intention)
            assertNotNull action
            myFixture.launchAction(action)
            VirtualFile file = myFixture.getTempDirFixture().getFile(expectedFileLocation)
            assertNotNull file
        } else {
            assert true
        }
    }

    protected void doScreenInspectionTestInCompound(String intention, String desc, String expectedFileLocation, String elName, boolean mustFind) {
        // move file because COMPOUND !...
        String file = "xml/${this.getTestName(false)}.xml"
        myFixture.moveFile(file, "zelda/widget")
        doElementCreateTest(intention, desc, expectedFileLocation, elName, 'screen', mustFind)
    }

    protected void doScreenInspectionTest(String intention, String desc, String elName, boolean mustFind) {
        doScreenInspectionTest(intention, desc, null, elName, mustFind)
    }

    protected void doFormInspectionTest(String intention, String desc, String elName, boolean mustFind) {
        doFormInspectionTest(intention, desc, null, elName, mustFind)
    }

    protected void doScreenInspectionTest(String intention, String desc, String expectedFileLocation, String elName, boolean mustFind) {
        doElementCreateTest(intention, desc, expectedFileLocation, elName, 'screen', mustFind)
    }

    protected void doFormInspectionTest(String intention, String desc, String expectedFileLocation, String elName, boolean mustFind) {
        doElementCreateTest(intention, desc, expectedFileLocation, elName, 'form', mustFind)
    }

    protected void doElementCreateTest(String intention, String desc, String expectedFileLocation, String elName,
                                       String elType, boolean mustFind) {
        myFixture.configureByFile(testFile)
        List<HighlightInfo> highlightInfos = myFixture.doHighlighting()
        List<String> highlightDescs = highlightInfos.collect { it.description }
        if (mustFind) {
            assertFalse highlightInfos.isEmpty()
            assert highlightDescs.contains(desc)
            final IntentionAction action = myFixture.findSingleIntention(intention)
            assertNotNull action
            myFixture.launchAction(action)
            PsiFile fileToLookIn = expectedFileLocation ? getExpectedFile(expectedFileLocation) : getFile()
            List<XmlTag> tags = PsiTreeUtil.collectElements(fileToLookIn, getTagFilter())
            assert tags.any { XmlTag tag ->
                tag.getAttribute('name')?.value == elName && tag.getName() == elType
            }
        } else {
            assertFalse highlightDescs.contains(desc)
        }
    }

    private PsiFile getExpectedFile(String expectedFileLocation) {
        VirtualFile virtualFile = myFixture.getTempDirFixture().getFile(expectedFileLocation)
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
