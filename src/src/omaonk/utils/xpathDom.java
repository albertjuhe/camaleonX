/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package omaonk.utils;

import java.io.*;
import java.util.ArrayList;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import javax.xml.transform.TransformerException;
import omaonk.structure.slideStructure;

/**
 *
 * @author Preproduccio
 */
public class xpathDom {

    private static Logger logger = Logger.getLogger(xpathDom.class.getName());
    private String xpath;
    private String xmlFile;
    private NodeList result;
    public  Document doc;

    public xpathDom(String xmlFile, String xpath) {
        this.xpath = xpath;
        this.xmlFile = xmlFile;
    }

    public NodeList execute()
            throws ParserConfigurationException, SAXException,
            IOException, XPathExpressionException {

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true); // never forget this!
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        this.doc = builder.parse(this.xmlFile);

        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(new relationshipNamespace());
        XPathExpression expr = xpath.compile(this.xpath);

        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        this.setResult((NodeList) result);
        return this.getResult();

    }

    public void addNodesRelationSlide(ArrayList aNodes) {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true); // never forget this!
        domFactory.setIgnoringComments(true);
        try {
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document doc = builder.parse(new File(this.xpath + this.xmlFile));

            NodeList nodes = doc.getElementsByTagName("Relationships");
            for (int i = 0; i < aNodes.size(); i++) {
                slideStructure sStr = (slideStructure)aNodes.get(i);
                Element relationshipNode = doc.createElementNS("http://schemas.openxmlformats.org/package/2006/relationships","Relationship");
                relationshipNode.setAttribute("Id", sStr.getrId());
                relationshipNode.setAttribute("Type", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/slide");
                relationshipNode.setAttribute("Target", sStr.getSlideFile());

                nodes.item(0).appendChild(relationshipNode);
            }
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(this.xpath + this.xmlFile));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

        } catch (SAXException sae) {
            logger.error(sae.getMessage());
        } catch (ParserConfigurationException pce) {
            logger.error(pce.getMessage());
        } catch (IOException ioe) {
            logger.error(ioe.getMessage());

        } catch (TransformerException tfe) {
            logger.error(tfe.getMessage());
        }

    }

    /**
     * @return the xpath
     */
    public String getXpath() {
        return xpath;
    }

    /**
     * @param xpath the xpath to set
     */
    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    /**
     * @return the xmlFile
     */
    public String getXmlFile() {
        return xmlFile;
    }

    /**
     * @param xmlFile the xmlFile to set
     */
    public void setXmlFile(String xmlFile) {
        this.xmlFile = xmlFile;
    }

    /**
     * @return the result
     */
    public NodeList getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(NodeList result) {
        this.result = result;
    }
}
