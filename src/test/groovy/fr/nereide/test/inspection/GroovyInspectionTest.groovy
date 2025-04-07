package fr.nereide.test.inspection

import fr.nereide.inspection.InspectionBundle
import fr.nereide.inspection.groovy.CacheOnNeverCacheEntityGroovyInspection
import fr.nereide.inspection.groovy.DuplicatedServiceGroovyInspection

class GroovyInspectionTest extends BaseInspectionTest {

    @Override
    String getLang() {
        return 'groovy'
    }

    void doNeverCacheTest(boolean mustFind) {
        myFixture.enableInspections(new CacheOnNeverCacheEntityGroovyInspection())
        doInspectionThenQuickFixTestWithFileEdit(mustFind,
                InspectionBundle.message('inspection.entity.cache.on.never.cache.use.quickfix'),
                InspectionBundle.message('inspection.entity.cache.on.never.cache.display.descriptor'))
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
        doHighlightTest(true, InspectionBundle.message('inspection.service.duplicate.display.descriptor'))
    }
}
