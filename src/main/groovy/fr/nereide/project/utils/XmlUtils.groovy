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

/**
 * Various xml utility methods
 */
class XmlUtils {

    /**
     * returns the xml tag parent of xml element or null if not found
     */
    static XmlTag getParentTag(XmlElement xmlElement) {
        if (!xmlElement) return null
        int i = 0
        PsiElement parent = xmlElement.parent
        while (i < 5 && parent && !(parent instanceof XmlTag)) {
            parent = parent.parent
        }
        return parent instanceof XmlTag ? parent : null
    }

    /**
     * Checks that the XmlElement we want reference on is in a file where it would be defined
     */
    static boolean isInRightFile(XmlAttributeValue value, Class fileType, DomManager dm) {
        PsiFile currentFile = value.containingFile as XmlFile
        return dm.getFileElement(currentFile, fileType) != null
    }

    static boolean isPageReferenceFromController(XmlTag containingTag) {
        return (containingTag.getAttribute('page') != null)
    }

    static String getScreenNameFromControllerString(XmlAttributeValue name) {
        Matcher matcher = Pattern.compile("[^#]*\$").matcher(name.value)
        return matcher.find() ? matcher.group(0) : null
    }

}
