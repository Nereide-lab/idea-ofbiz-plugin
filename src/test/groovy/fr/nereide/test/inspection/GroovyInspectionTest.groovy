package fr.nereide.test.inspection

import fr.nereide.inspection.groovy.CacheOnNeverCacheEntityGroovyInspection
import fr.nereide.inspection.groovy.DuplicatedEntityGroovyInspection
import fr.nereide.inspection.groovy.DuplicatedServiceGroovyInspection
import fr.nereide.inspection.groovy.ServiceNotFoundInGroovyInspection

import static fr.nereide.inspection.InspectionBundle.message

class GroovyInspectionTest extends BaseInspectionTest {

    @Override
    String getLang() {
        return 'groovy'
    }

    void doNeverCacheTest(boolean mustFind) {
        myFixture.enableInspections(new CacheOnNeverCacheEntityGroovyInspection())
        doInspectionThenQuickFixTestWithFileEdit(mustFind,
                message('inspection.entity.cache.on.never.cache.use.quickfix'),
                message('inspection.entity.cache.on.never.cache.display.descriptor'))
    }

    void testCacheOnNeverCacheEntityInCompiledGroovy() {
        doNeverCacheTest(true)
    }

    void testCacheOnNeverCacheEntityIGroovyScript() {
        doNeverCacheTest(true)
    }

    void testCacheOnNeverCacheEntityIGroovyScriptWithTrueParameter() {
        doNeverCacheTest(true)
    }

    void testCacheOnNeverCacheEntityIGroovyScriptWithFalseParameter() {
        doNeverCacheTest(false)
    }

    void testDuplicatedServiceInspection() {
        myFixture.enableInspections(new DuplicatedServiceGroovyInspection())
        myFixture.configureByFile(testFile)
        doHighlightTest(true, message('inspection.service.duplicate.display.descriptor'))
    }

    void testDuplicatedEntityInspection() {
        myFixture.enableInspections(new DuplicatedEntityGroovyInspection())
        myFixture.configureByFile(testFile)
        doHighlightTest(true, message('inspection.entity.duplicate.display.descriptor'))
    }

    void doServiceTest(boolean shouldFind) {
        myFixture.enableInspections(new ServiceNotFoundInGroovyInspection())
        myFixture.configureByFile(testFile)
        doHighlightTest(shouldFind, message('inspection.service.not.found.display.descriptor'))
    }

    void testServiceNotFoundInspection() {
        doServiceTest(true)
    }

    void testServiceNotFoundInspectionSafety() {
        doServiceTest(false)
    }
}
