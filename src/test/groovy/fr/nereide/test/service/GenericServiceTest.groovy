package fr.nereide.test.service

import com.intellij.lang.properties.references.PropertyReference
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.impl.PsiMultiReference
import fr.nereide.test.GenericOfbizPluginTestCase

class GenericServiceTest extends GenericOfbizPluginTestCase {

    @Override
    protected void setUp() {
        super.setUp()
        myFixture.copyDirectoryToProject('assets', '')
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/testData/service"
    }

    /**
     * Temporary workaround for tests to stay green
     */
    void testDummy() {
        assert true
    }
}
