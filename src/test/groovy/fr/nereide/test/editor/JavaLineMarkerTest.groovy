package fr.nereide.test.editor

import com.intellij.psi.PsiLiteralExpression
import fr.nereide.editor.marker.java.JavaEntityEcaMarkerProvider
import fr.nereide.editor.marker.java.JavaExtendedEntityMarkerProvider
import fr.nereide.editor.marker.java.JavaServiceEcaEcaMarkerProvider

/**
 * Line markers and gutter icons tests in java
 */
class JavaLineMarkerTest extends BaseLineMarkerTest {

    String getExtension() { return 'java' }

    Class getElementTypeToFind() { return PsiLiteralExpression }

    void testServiceEcaMarkerInJava() {
        doTest(new JavaServiceEcaEcaMarkerProvider(), '1 ECA(s) present on service')
    }

    void testEntityEcaMarkerInJava() {
        doTest(new JavaEntityEcaMarkerProvider(), '1 ECA(s) present on entity')
    }

    void testExtendedEntityMarkerInJava() {
        doTest(new JavaExtendedEntityMarkerProvider(), 'Entity is extended')
    }

}
