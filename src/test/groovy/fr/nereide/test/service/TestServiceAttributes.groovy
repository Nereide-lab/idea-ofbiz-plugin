package fr.nereide.test.service


import fr.nereide.dom.ServiceDefFile.Service
import fr.nereide.project.ProjectServiceInterface
import fr.nereide.project.worker.ServiceWorker

class TestServiceAttributes extends GenericServiceTest {

    void testVanillaServiceAttributes() {
        ProjectServiceInterface ps = myFixture.getProject().getService(ProjectServiceInterface.class)
        Service service = ps.getService('ping')
        List<String> requiredAttributes = geRequiredAttrNames(service, ps)
        List<String> optionalAttributes = getOptionalAttrNames(service, ps)
        assertContainsElements(requiredAttributes, ['hello', 'world'])
        assertContainsElements(optionalAttributes, ['bye'])
    }

    void testEntityAutoAttributesAllOptional() {
        final List EXPECTED_OPTIONALS = ['rifle', 'knife', 'mapId']
        ProjectServiceInterface ps = myFixture.getProject().getService(ProjectServiceInterface.class)
        Service service = ps.getService('createNavezgane')
        List<String> requiredAttributes = geRequiredAttrNames(service, ps)
        List<String> optionalAttributes = getOptionalAttrNames(service, ps)
        assertEmpty(requiredAttributes)
        assertContainsElements(optionalAttributes, EXPECTED_OPTIONALS)
    }

    void testEntityAutoAttributes() {
        final List EXPECTED_REQUIRED = ['slaves', 'nemesis']
        final List EXPECTED_OPTIONALS = ['tooth', 'blood', 'magic']
        ProjectServiceInterface ps = myFixture.getProject().getService(ProjectServiceInterface.class)
        Service service = ps.getService('killDracula')
        List<String> requiredAttributes = geRequiredAttrNames(service, ps)
        List<String> optionalAttributes = getOptionalAttrNames(service, ps)
        assertContainsElements(requiredAttributes, EXPECTED_REQUIRED)
        assertDoesntContain(requiredAttributes, EXPECTED_OPTIONALS)
        assertContainsElements(optionalAttributes, EXPECTED_OPTIONALS)
        assertDoesntContain(optionalAttributes, EXPECTED_REQUIRED)
    }

    private static List<String> geRequiredAttrNames(Service service, ProjectServiceInterface ps) {
        ServiceWorker.getRequiredInAttributes(service, ps)
                .stream().map { it.get(ServiceWorker.SERVICE_ATTR_NAME) }.collect()
    }

    private static List<String> getOptionalAttrNames(Service service, ProjectServiceInterface ps) {
        ServiceWorker.getOptionalInAttributes(service, ps)
                .stream().map { it.get(ServiceWorker.SERVICE_ATTR_NAME) }.collect()
    }
}
