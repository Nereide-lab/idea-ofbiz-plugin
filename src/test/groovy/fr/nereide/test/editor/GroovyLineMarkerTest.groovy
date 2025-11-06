package fr.nereide.test.editor

import com.intellij.psi.PsiElement
import fr.nereide.editor.marker.groovy.GroovyEntityEcaMarkerProvider
import fr.nereide.editor.marker.groovy.GroovyExtendedEntityMarkerProvider
import fr.nereide.editor.marker.groovy.GroovyServiceEcaEcaMarkerProvider

/**
 * Line markers and gutter icons tests in groovy
 */
class GroovyLineMarkerTest extends BaseLineMarkerTest {

    /* codenarc-disable JUnitTestMethodWithoutAssert */

    void testServiceEcaMarkerInGroovy() {
        doTest(new GroovyServiceEcaEcaMarkerProvider(), '1 ECA(s) present on service')
    }

    void testEntityEcaMarkerInGroovy() {
        doTest(new GroovyEntityEcaMarkerProvider(), '1 ECA(s) present on entity')
    }

    void testExtendedEntityMarkerInGroovy() {
        doTest(new GroovyExtendedEntityMarkerProvider(), 'Entity is extended')
    }
    /* codenarc-enable JUnitTestMethodWithoutAssert */

    protected String getExtension() { return 'groovy' }

    protected Class getElementTypeToFind() { return PsiElement }

}
