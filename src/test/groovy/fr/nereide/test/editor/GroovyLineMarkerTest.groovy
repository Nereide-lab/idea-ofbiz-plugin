package fr.nereide.test.editor

import com.intellij.psi.PsiElement
import fr.nereide.editor.marker.groovy.GroovyEntityEcaMarkerProvider
import fr.nereide.editor.marker.groovy.GroovyServiceEcaMarkerProvider

class GroovyLineMarkerTest extends BaseLineMarkerTest {

    protected String getExtension() { return 'groovy' }

    @Override
    protected Class getElementTypeToFind() {
        return PsiElement.class
    }

    void testServiceEcaMarkerInGroovy() {
        doTest(new GroovyServiceEcaMarkerProvider(), '1 ECA(s) present on service')
    }

    void testEntityEcaMarkerInGroovy() {
        doTest(new GroovyEntityEcaMarkerProvider(), '1 ECA(s) present on entity')
    }
}
