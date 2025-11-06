package fr.nereide.test.service

import fr.nereide.dom.element.service.Service
import fr.nereide.project.OfbizProjectHelper
import fr.nereide.project.worker.ServiceWorker

/**
 * Service worker tests
 */
class TestServiceAttributes extends BaseServiceTest {

    void testVanillaServiceAttributes() {
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(myFixture.project)
        Service service = ph.getService('ping')
        List<String> requiredAttributes = getRequiredAttrNames(service, ph)
        List<String> optionalAttributes = getOptionalAttrNames(service, ph)
        assertContainsElements(requiredAttributes, ['hello', 'world'])
        assertContainsElements(optionalAttributes, ['bye'])
    }

    void testEntityAutoAttributesAllOptional() {
        final List expectedOptionals = ['rifle', 'knife', 'mapId']
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(myFixture.project)
        Service service = ph.getService('createNavezgane')
        List<String> requiredAttributes = getRequiredAttrNames(service, ph)
        List<String> optionalAttributes = getOptionalAttrNames(service, ph)
        assertEmpty(requiredAttributes)
        assertContainsElements(optionalAttributes, expectedOptionals)
    }

    void testEntityAutoAttributes() {
        final List expectedRequired = ['slaves', 'nemesis']
        final List expectedOptionals = ['tooth', 'blood', 'magic']
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(myFixture.project)
        Service service = ph.getService('killDracula')
        List<String> requiredAttributes = getRequiredAttrNames(service, ph)
        List<String> optionalAttributes = getOptionalAttrNames(service, ph)
        assertContainsElements(requiredAttributes, expectedRequired)
        assertDoesntContain(requiredAttributes, expectedOptionals)
        assertContainsElements(optionalAttributes, expectedOptionals)
        assertDoesntContain(optionalAttributes, expectedRequired)
    }

    private static List<String> getRequiredAttrNames(Service service, OfbizProjectHelper ph) {
        return ServiceWorker.getRequiredInAttributes(service, ph)*.get(ServiceWorker.SERVICE_ATTR_NAME) as List<String>
    }

    private static List<String> getOptionalAttrNames(Service service, OfbizProjectHelper ph) {
        return ServiceWorker.getOptionalInAttributes(service, ph)*.get(ServiceWorker.SERVICE_ATTR_NAME) as List<String>
    }

}
