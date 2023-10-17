package fr.nereide.test.inspection

import fr.nereide.inspection.InspectionBundle
import fr.nereide.inspection.groovy.CacheOnNeverCacheEntityGroovyInspection

class GroovyInspectionTest extends BaseInspectionTest {

    CacheOnNeverCacheEntityGroovyInspection myCacheInsp

    GroovyInspectionTest() {
        myCacheInsp = new CacheOnNeverCacheEntityGroovyInspection()
    }

    @Override
    String getLang() {
        return 'groovy'
    }

    void testCacheOnNeverCacheEntityInCompiledGroovy() {
        String intention = InspectionBundle.message('inspection.entity.cache.on.never.cache.use.quickfix')
        myFixture.enableInspections(myCacheInsp)
        doFileFixTest(intention)
    }

    void testCacheOnNeverCacheEntityIGroovyScript() {
        String intention = InspectionBundle.message('inspection.entity.cache.on.never.cache.use.quickfix')
        myFixture.enableInspections(myCacheInsp)
        doFileFixTest(intention)
    }

    void testCacheOnNeverCacheEntityIGroovyScriptWithTrueParameter() {
        String intention = InspectionBundle.message('inspection.entity.cache.on.never.cache.use.quickfix')
        myFixture.enableInspections(myCacheInsp)
        doFileFixTest(intention)
    }

    void testCacheOnNeverCacheEntityIGroovyScriptWithFalseParameter() {
        String intention = InspectionBundle.message('inspection.entity.cache.on.never.cache.use.quickfix')
        myFixture.enableInspections(myCacheInsp)
        String desc = InspectionBundle.message('inspection.entity.cache.on.never.cache.display.descriptor')
        doFileFixTest(intention, desc, false)
    }
}
