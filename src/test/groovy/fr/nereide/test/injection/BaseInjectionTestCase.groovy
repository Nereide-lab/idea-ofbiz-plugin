package fr.nereide.test.injection

import com.intellij.psi.PsiElement
import fr.nereide.test.BaseOfbizPluginTestCase
import org.junit.Ignore

@Ignore('Parent class, No tests here')
class BaseInjectionTestCase extends BaseOfbizPluginTestCase {

    protected doTest(Class expectedLang) {
        String file = "${this.getTestName(false)}.xml"
        myFixture.configureByFile(file)
        PsiElement elementAtCaret = myFixture.file.findElementAt(myFixture.caretOffset)
        assert expectedLang.isAssignableFrom(elementAtCaret.language.getClass())
    }
}
