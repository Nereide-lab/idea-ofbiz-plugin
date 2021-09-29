package org.apache.ofbiz.reference.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import com.intellij.util.xml.DomElement
import org.apache.ofbiz.dom.ScreenFile
import org.apache.ofbiz.project.ProjectServiceInterface
import org.jetbrains.annotations.Nullable

import java.util.regex.Matcher
import java.util.regex.Pattern

class ScreenReference extends GenericXmlReference {

    ScreenReference(XmlAttributeValue formName, String elementName, boolean soft) {
        super(formName, soft, elementName,
                "getScreens",
                "getName",
                ScreenFile.class)
    }

    @Nullable
    PsiElement resolve() {
        super.resolve()
    }
}