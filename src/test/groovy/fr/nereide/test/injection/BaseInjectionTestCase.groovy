package fr.nereide.test.injection

import com.intellij.psi.PsiElement
import fr.nereide.test.GenericOfbizPluginTestCase

class BaseInjectionTestCase extends GenericOfbizPluginTestCase {

    protected doTest(Class expectedLang) {
        String file = "${this.getTestName(false).replaceAll('test', '')}.xml"
        myFixture.configureByFile(file)
        PsiElement elementAtCaret = myFixture.file.findElementAt(myFixture.caretOffset)
        assert expectedLang.isAssignableFrom(elementAtCaret.language.getClass())
    }
}
