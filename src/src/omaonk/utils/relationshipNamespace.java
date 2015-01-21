/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package omaonk.utils;

import java.util.Iterator;
import javax.xml.*;
import javax.xml.namespace.NamespaceContext;

/**
 *
 * @author Preproduccio
 */
public class relationshipNamespace implements NamespaceContext {

    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new NullPointerException("Null prefix");
        } else if ("relation".equals(prefix)) {
            return "http://schemas.openxmlformats.org/package/2006/relationships";
        } else if ("wp".equals(prefix)) {
          return "http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing";
        } else if ("xml".equals(prefix)) {
            return XMLConstants.XML_NS_URI;
        }
        return XMLConstants.NULL_NS_URI;
    }

    // This method isn't necessary for XPath processing.
    public String getPrefix(String uri) {
        throw new UnsupportedOperationException();
    }

    // This method isn't necessary for XPath processing either.
    public Iterator getPrefixes(String uri) {
        throw new UnsupportedOperationException();
    }
}
