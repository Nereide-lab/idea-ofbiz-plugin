package fr.nereide.test.service

import fr.nereide.test.BaseOfbizPluginTestCase
import org.junit.Ignore

@Ignore('Setup class, No tests here')
class BaseServiceTest extends BaseOfbizPluginTestCase {

    @Override
    protected void setUp() {
        super.setUp()
        myFixture.copyDirectoryToProject('assets', '')
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/testData/service"
    }
}
