package fr.nereide.project.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.intellij.util.xml.DomManager

import java.util.regex.Matcher
import java.util.regex.Pattern

class XmlUtils {

    /**
     * returns the xml tag parent of xml element or null if not found
     * @param xmlElement
     * @return
     */
    static XmlTag getParentTag(XmlElement xmlElement) {
        int i = 0
        PsiElement parent = xmlElement.getParent()
        while (i < 5 && !(parent instanceof XmlTag)) {
            parent = parent.getParent()
        }
        return parent instanceof XmlTag ? parent : null
    }

    /**
     * Checks that the XmlElement we want reference on is in a file where it would be defined
     * @param value
     * @param fileType
     * @param dm
     * @return
     */
    static boolean isInRightFile(XmlAttributeValue value, Class fileType, DomManager dm) {
        PsiFile currentFile = value.getContainingFile() as XmlFile
        return dm.getFileElement(currentFile, fileType) != null
    }

    static boolean isPageReferenceFromController(XmlTag containingTag) {
        return (containingTag.getAttribute('page') != null)
    }

    static String getScreenNameFromControllerString(XmlAttributeValue name) {
        final Pattern SCREEN_NAME_PATTERN = Pattern.compile("[^#]*\$")
        Matcher matcher = SCREEN_NAME_PATTERN.matcher(name.getValue())
        return matcher.find() ? matcher.group(0) : null
    }
}
