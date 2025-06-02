package org.apache.ofbiz;
import org.apache.ofbiz.service.LocalDispatcher;

class ServiceEcaMarkerInJava {
    void myTestFunction() {
        LocalDispatcher dispatcher = new LocalDispatcher();
        var result = dispatcher.runSync("ServiceEcaMarkerInJavaElement", null);
    }
}
