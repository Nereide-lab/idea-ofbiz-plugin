package fr.nereide.test.editor

import com.intellij.psi.PsiLiteralExpression
import fr.nereide.editor.marker.java.JavaServiceEcaMarkerProvider

class JavaLineMarkerTest extends BaseLineMarkerTest {

    protected String getExtension() { return 'java' }

    @Override
    protected Class getElementTypeToFind() {
        return PsiLiteralExpression.class
    }

    void testServiceEcaMarkerInJava() {
        doTest(new JavaServiceEcaMarkerProvider(), 'Eca(s) present on service')
    }
}
