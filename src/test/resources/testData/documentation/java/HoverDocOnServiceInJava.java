
package org.apache.ofbiz;

import org.apache.ofbiz.service.LocalDispatcher;

public class HoverDocOnServiceInJava {
    public void myTestFunction() {
        LocalDispatcher dispatcher = new LocalDispatcher();
        var result = dispatcher.runSync("HelloW<caret>orld");
    }
}
