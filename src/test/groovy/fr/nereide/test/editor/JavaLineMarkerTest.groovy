package fr.nereide.test.editor

import com.intellij.psi.PsiLiteralExpression
import fr.nereide.editor.marker.java.JavaEntityEcaMarkerProvider
import fr.nereide.editor.marker.java.JavaServiceEcaEcaMarkerProvider

class JavaLineMarkerTest extends BaseLineMarkerTest {

    String getExtension() { return 'java' }

    Class getElementTypeToFind() { return PsiLiteralExpression.class }

    void testServiceEcaMarkerInJava() {
        doTest(new JavaServiceEcaEcaMarkerProvider(), '1 ECA(s) present on service')
    }

    void testEntityEcaMarkerInJava() {
        doTest(new JavaEntityEcaMarkerProvider(), '1 ECA(s) present on entity')
    }
}
