package org.apache.ofbiz.idea.plugin.test.service

class TestServiceAttributes extends BaseServiceTest {

    void testVanillaServiceAttributes() {
        org.apache.ofbiz.idea.plugin.project.ProjectServiceInterface ps = myFixture.getProject().getService(org.apache.ofbiz.idea.plugin.project.ProjectServiceInterface.class)
        org.apache.ofbiz.idea.plugin.dom.ServiceDefFile.Service service = ps.getService('ping')
        List<String> requiredAttributes = geRequiredAttrNames(service, ps)
        List<String> optionalAttributes = getOptionalAttrNames(service, ps)
        assertContainsElements(requiredAttributes, ['hello', 'world'])
        assertContainsElements(optionalAttributes, ['bye'])
    }

    void testEntityAutoAttributesAllOptional() {
        final List EXPECTED_OPTIONALS = ['rifle', 'knife', 'mapId']
        org.apache.ofbiz.idea.plugin.project.ProjectServiceInterface ps = myFixture.getProject().getService(org.apache.ofbiz.idea.plugin.project.ProjectServiceInterface.class)
        org.apache.ofbiz.idea.plugin.dom.ServiceDefFile.Service service = ps.getService('createNavezgane')
        List<String> requiredAttributes = geRequiredAttrNames(service, ps)
        List<String> optionalAttributes = getOptionalAttrNames(service, ps)
        assertEmpty(requiredAttributes)
        assertContainsElements(optionalAttributes, EXPECTED_OPTIONALS)
    }

    void testEntityAutoAttributes() {
        final List EXPECTED_REQUIRED = ['slaves', 'nemesis']
        final List EXPECTED_OPTIONALS = ['tooth', 'blood', 'magic']
        org.apache.ofbiz.idea.plugin.project.ProjectServiceInterface ps = myFixture.getProject().getService(org.apache.ofbiz.idea.plugin.project.ProjectServiceInterface.class)
        org.apache.ofbiz.idea.plugin.dom.ServiceDefFile.Service service = ps.getService('killDracula')
        List<String> requiredAttributes = geRequiredAttrNames(service, ps)
        List<String> optionalAttributes = getOptionalAttrNames(service, ps)
        assertContainsElements(requiredAttributes, EXPECTED_REQUIRED)
        assertDoesntContain(requiredAttributes, EXPECTED_OPTIONALS)
        assertContainsElements(optionalAttributes, EXPECTED_OPTIONALS)
        assertDoesntContain(optionalAttributes, EXPECTED_REQUIRED)
    }

    private static List<String> geRequiredAttrNames(org.apache.ofbiz.idea.plugin.dom.ServiceDefFile.Service service, org.apache.ofbiz.idea.plugin.project.ProjectServiceInterface ps) {
        org.apache.ofbiz.idea.plugin.project.worker.ServiceWorker.getRequiredInAttributes(service, ps)
                .stream().map { it.get(org.apache.ofbiz.idea.plugin.project.worker.ServiceWorker.SERVICE_ATTR_NAME) }.collect()
    }

    private static List<String> getOptionalAttrNames(org.apache.ofbiz.idea.plugin.dom.ServiceDefFile.Service service, org.apache.ofbiz.idea.plugin.project.ProjectServiceInterface ps) {
        org.apache.ofbiz.idea.plugin.project.worker.ServiceWorker.getOptionalInAttributes(service, ps)
                .stream().map { it.get(org.apache.ofbiz.idea.plugin.project.worker.ServiceWorker.SERVICE_ATTR_NAME) }.collect()
    }
}
