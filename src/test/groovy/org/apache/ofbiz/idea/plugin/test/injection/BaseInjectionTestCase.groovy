package org.apache.ofbiz.idea.plugin.test.injection

import com.intellij.psi.PsiElement
import org.junit.Ignore

@Ignore('Parent class, No tests here')
class BaseInjectionTestCase extends org.apache.ofbiz.idea.plugin.test.BaseOfbizPluginTestCase {

    protected doTest(Class expectedLang) {
        String file = "${this.getTestName(false)}.xml"
        myFixture.configureByFile(file)
        PsiElement elementAtCaret = myFixture.file.findElementAt(myFixture.caretOffset)
        assert expectedLang.isAssignableFrom(elementAtCaret.language.getClass())
    }
}
