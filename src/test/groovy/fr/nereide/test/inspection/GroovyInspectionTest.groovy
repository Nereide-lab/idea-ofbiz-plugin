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
        doTest(intention)
    }

}
