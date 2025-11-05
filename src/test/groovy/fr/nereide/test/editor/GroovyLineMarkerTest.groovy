package fr.nereide.test.editor

import com.intellij.psi.PsiElement
import fr.nereide.editor.marker.groovy.GroovyEntityEcaMarkerProvider
import fr.nereide.editor.marker.groovy.GroovyExtendedEntityMarkerProvider
import fr.nereide.editor.marker.groovy.GroovyServiceEcaEcaMarkerProvider

/**
 * Line markers and gutter icons tests in groovy
 */
class GroovyLineMarkerTest extends BaseLineMarkerTest {

    String getExtension() { return 'groovy' }

    Class getElementTypeToFind() { return PsiElement.class }

    void testServiceEcaMarkerInGroovy() {
        doTest(new GroovyServiceEcaEcaMarkerProvider(), '1 ECA(s) present on service')
    }

    void testEntityEcaMarkerInGroovy() {
        doTest(new GroovyEntityEcaMarkerProvider(), '1 ECA(s) present on entity')
    }

    void testExtendedEntityMarkerInGroovy() {
        doTest(new GroovyExtendedEntityMarkerProvider(), 'Entity is extended')
    }

}
