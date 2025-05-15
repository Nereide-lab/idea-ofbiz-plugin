package fr.nereide.test.inspection

import fr.nereide.inspection.groovy.CacheOnNeverCacheEntityGroovyInspection
import fr.nereide.inspection.groovy.DuplicatedEntityGroovyInspection
import fr.nereide.inspection.groovy.DuplicatedServiceGroovyInspection
import fr.nereide.inspection.groovy.EntityNotFoundInGroovyInspection
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
        doHighlightTest(true, message('inspection.service.duplicate.display.descriptor'),
                new DuplicatedServiceGroovyInspection())
    }

    void testDuplicatedEntityInspection() {
        doHighlightTest(true, message('inspection.entity.duplicate.display.descriptor'),
                new DuplicatedEntityGroovyInspection())
    }

    void testServiceNotFoundInspection() {
        doHighlightTest(true, message('inspection.service.not.found.display.descriptor'),
                new ServiceNotFoundInGroovyInspection())
    }

    void testServiceNotFoundInspectionSafety() {
        doHighlightTest(false, message('inspection.service.not.found.display.descriptor'),
                new ServiceNotFoundInGroovyInspection())
    }

    void testEntityNotFoundInspection() {
        doHighlightTest(true, message('inspection.entity.not.found.display.descriptor'),
                new EntityNotFoundInGroovyInspection())
    }

    void testEntityNotFoundInspectionSafety() {
        doHighlightTest(false, message('inspection.entity.not.found.display.descriptor'),
                new EntityNotFoundInGroovyInspection())
    }
}
