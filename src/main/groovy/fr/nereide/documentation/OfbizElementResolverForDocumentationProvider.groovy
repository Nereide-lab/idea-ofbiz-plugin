package fr.nereide.documentation

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import fr.nereide.project.OfbizPatterns
import fr.nereide.project.utils.MiscUtils
import fr.nereide.reference.java.EntityJavaReference
import fr.nereide.reference.java.ServiceJavaReference
import fr.nereide.reference.xml.EntityReference
import fr.nereide.reference.xml.ServiceReference
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

class OfbizElementResolverForDocumentationProvider extends AbstractDocumentationProvider {

    private static final Logger LOG = Logger.getInstance(OfbizElementResolverForDocumentationProvider.class)

    @Override
    PsiElement getCustomDocumentationElement(@NotNull Editor editor, @NotNull PsiFile file, @Nullable PsiElement contextElement, int targetOffset) {
        // TODO : rajouter les autres types possibles
        PsiElement xmlAttr = PsiTreeUtil.getParentOfType(contextElement, XmlAttribute.class)
        /*************
         *    XML    *
         *************/
        if (xmlAttr) {
            PsiElement xmlAttrValue = PsiTreeUtil.getChildOfType(xmlAttr, XmlAttributeValue)
            if (OfbizPatterns.XML.SERVICE_DEF_CALL.accepts(xmlAttr)) {
                return xmlAttrValue ? resolveService(xmlAttrValue) : null
            } else if (OfbizPatterns.XML.ENTITY_CALL.accepts(xmlAttr)) {
                return xmlAttrValue ? resolveEntityOrView(xmlAttrValue) : null
            }
        }
        /*************
         *    JAVA   *
         *************/
        if (OfbizPatterns.JAVA.SERVICE_CALL.accepts(contextElement)) {
            return resolveService(PsiTreeUtil.getParentOfType(contextElement, PsiLiteralExpression.class))
        }
        if (OfbizPatterns.JAVA.ENTITY_CALL.accepts(contextElement)) {
            return resolveEntityOrView(PsiTreeUtil.getParentOfType(contextElement, PsiLiteralExpression.class))
        }
        /*************
         *  GROOVY   *
         * useless ? *
         *************/
        return null
    }

    private static PsiElement resolveEntityOrView(XmlAttributeValue attr) {
        EntityReference entity = new EntityReference(attr, true)
        return entity.resolve()
    }

    private static PsiElement resolveService(XmlAttributeValue attr) {
        ServiceReference service = new ServiceReference(attr, true)
        return service.resolve()
    }

    private static PsiElement resolveService(PsiLiteralExpression expr) {
        ServiceJavaReference service = new ServiceJavaReference(expr, true)
        return service.resolve()
    }

    private static PsiElement resolveEntityOrView(PsiLiteralExpression expr) {
        EntityJavaReference entity = new EntityJavaReference(expr, true)
        return entity.resolve()
    }
}
