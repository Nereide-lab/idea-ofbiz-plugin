/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * 'License') you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * 'AS IS' BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package fr.nereide.test.editor

import com.intellij.psi.xml.XmlAttributeValue
import fr.nereide.editor.marker.xml.XmlEntityEcaMarkerProvider
import fr.nereide.editor.marker.xml.XmlExtendedEntityMarkerProvider
import fr.nereide.editor.marker.xml.XmlServiceEcaEcaMarkerProvider

/**
 * Line markers and gutter icons tests in xml
 */
// codenarc-disable JUnitTestMethodWithoutAssert
class XmlLineMarkerTests extends BaseLineMarkerTest {

    void testServiceEcaMarkerInXml() {
        doTest(new XmlServiceEcaEcaMarkerProvider(), '1 ECA(s) present on service')
    }

    void testEntityEcaMarkerInXml() {
        doTest(new XmlEntityEcaMarkerProvider(), '1 ECA(s) present on entity')
    }

    void testExtendedEntityMarkerInXml() {
        doTest(new XmlExtendedEntityMarkerProvider(), 'Entity is extended')
    }

    protected String getExtension() { return 'xml' }

    protected Class getElementTypeToFind() { return XmlAttributeValue }

}
