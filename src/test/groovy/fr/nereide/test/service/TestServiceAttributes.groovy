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
        final List EXPECTED_OPTIONALS = ['rifle', 'knife', 'mapId']
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(myFixture.project)
        Service service = ph.getService('createNavezgane')
        List<String> requiredAttributes = getRequiredAttrNames(service, ph)
        List<String> optionalAttributes = getOptionalAttrNames(service, ph)
        assertEmpty(requiredAttributes)
        assertContainsElements(optionalAttributes, EXPECTED_OPTIONALS)
    }

    void testEntityAutoAttributes() {
        final List EXPECTED_REQUIRED = ['slaves', 'nemesis']
        final List EXPECTED_OPTIONALS = ['tooth', 'blood', 'magic']
        OfbizProjectHelper ph = OfbizProjectHelper.getInstance(myFixture.project)
        Service service = ph.getService('killDracula')
        List<String> requiredAttributes = getRequiredAttrNames(service, ph)
        List<String> optionalAttributes = getOptionalAttrNames(service, ph)
        assertContainsElements(requiredAttributes, EXPECTED_REQUIRED)
        assertDoesntContain(requiredAttributes, EXPECTED_OPTIONALS)
        assertContainsElements(optionalAttributes, EXPECTED_OPTIONALS)
        assertDoesntContain(optionalAttributes, EXPECTED_REQUIRED)
    }

    private static List<String> getRequiredAttrNames(Service service, OfbizProjectHelper ph) {
        ServiceWorker.getRequiredInAttributes(service, ph)
                .collect { it -> it.get(ServiceWorker.SERVICE_ATTR_NAME) } as List<String>
    }

    private static List<String> getOptionalAttrNames(Service service, OfbizProjectHelper ph) {
        ServiceWorker.getOptionalInAttributes(service, ph)
                .collect { it -> it.get(ServiceWorker.SERVICE_ATTR_NAME) } as List<String>
    }

}
