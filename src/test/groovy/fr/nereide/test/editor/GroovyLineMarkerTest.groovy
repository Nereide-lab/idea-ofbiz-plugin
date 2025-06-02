package fr.nereide.test.editor

import com.intellij.psi.PsiElement
import fr.nereide.editor.marker.groovy.GroovyServiceEcaMarkerProvider

class GroovyLineMarkerTest extends BaseLineMarkerTest {

    protected String getExtension() { return 'groovy' }

    @Override
    protected Class getElementTypeToFind() {
        return PsiElement.class
    }

    void testServiceEcaMarkerInGroovy() {
        doTest(new GroovyServiceEcaMarkerProvider(), 'Eca(s) present on service')
    }
}
