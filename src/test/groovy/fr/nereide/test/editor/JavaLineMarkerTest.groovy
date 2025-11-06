package fr.nereide.test.editor

import com.intellij.psi.PsiLiteralExpression
import fr.nereide.editor.marker.java.JavaEntityEcaMarkerProvider
import fr.nereide.editor.marker.java.JavaExtendedEntityMarkerProvider
import fr.nereide.editor.marker.java.JavaServiceEcaEcaMarkerProvider

/**
 * Line markers and gutter icons tests in java
 */
class JavaLineMarkerTest extends BaseLineMarkerTest {

    /* codenarc-disable JUnitTestMethodWithoutAssert */

    void testServiceEcaMarkerInJava() {
        doTest(new JavaServiceEcaEcaMarkerProvider(), '1 ECA(s) present on service')
    }

    void testEntityEcaMarkerInJava() {
        doTest(new JavaEntityEcaMarkerProvider(), '1 ECA(s) present on entity')
    }

    void testExtendedEntityMarkerInJava() {
        doTest(new JavaExtendedEntityMarkerProvider(), 'Entity is extended')
    }
    /* codenarc-enable JUnitTestMethodWithoutAssert */

    protected String getExtension() { return 'java' }

    protected Class getElementTypeToFind() { return PsiLiteralExpression }

}
