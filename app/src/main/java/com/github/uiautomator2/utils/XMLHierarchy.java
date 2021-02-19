package com.github.uiautomator2.utils;

import android.view.accessibility.AccessibilityNodeInfo;

import com.github.uiautomator2.common.exceptions.InvalidSelectorException;
import com.github.uiautomator2.common.exceptions.UiAutomator2Exception;
import com.github.uiautomator2.core.AccessibilityNodeInfoDumper;
import com.github.uiautomator2.model.XPathFinder;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.HashMap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


public abstract class XMLHierarchy {


    private static XPathExpression compileXpath(String xpathExpression) throws InvalidSelectorException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression exp = null;
        try {
            exp = xpath.compile(xpathExpression);
        } catch (XPathExpressionException e) {
            throw new InvalidSelectorException("Invalid XPath expression: ", e);
        }
        return exp;
    }

    public static InputSource getRawXMLHierarchy() throws UiAutomator2Exception {
        AccessibilityNodeInfo root = XPathFinder.getRootAccessibilityNode();
        return getRawXMLHierarchy(root);
    }


    public static InputSource getRawXMLHierarchy(AccessibilityNodeInfo root) throws UiAutomator2Exception {
        String xmlDump = AccessibilityNodeInfoDumper.getWindowXMLHierarchy(root);
        return new InputSource(new StringReader(xmlDump));
    }

    /**
     * 获取字符串形式
     * @return
     * @throws UiAutomator2Exception
     */
    public static String getRawStringHierarchy() throws UiAutomator2Exception {
        AccessibilityNodeInfo root = XPathFinder.getCurstomRootAccessibilityNode();
        return AccessibilityNodeInfoDumper.getWindowXMLHierarchy(root);
    }

    public static Node getFormattedXMLDoc() throws UiAutomator2Exception {
        return formatXMLInput(getRawXMLHierarchy());
    }


    public static Node formatXMLInput(InputSource input) {
        XPath xpath = XPathFactory.newInstance().newXPath();

        Node root = null;
        try {
            root = (Node) xpath.evaluate("/", input, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            throw new RuntimeException("Could not read xml hierarchy: ", e);
        }

        HashMap<String, Integer> instances = new HashMap<String, Integer>();

        // rename all the nodes with their "class" attribute
        // add an instance attribute
        annotateNodes(root, instances);

        return root;
    }


    private static void annotateNodes(Node node, HashMap<String, Integer> instances) {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                visitNode(children.item(i), instances);
                annotateNodes(children.item(i), instances);
            }
        }
    }

    // set the node's tag name to the same as it's android class.
    // also number all instances of each class with an "instance" number. It increments
    // for each class separately.
    // this allows use to use class and instance to identify a node.
    // we also take this chance to clean class names that might have dollar signs in
    // them (and other odd characters)
    private static void visitNode(Node node, HashMap<String, Integer> instances) {

        Document doc = node.getOwnerDocument();
        NamedNodeMap attributes = node.getAttributes();

        String androidClass;
        try {
            androidClass = attributes.getNamedItem("class").getNodeValue();
        } catch (Exception e) {
            return;
        }

        androidClass = cleanTagName(androidClass);

        if (!instances.containsKey(androidClass)) {
            instances.put(androidClass, 0);
        }
        Integer instance = instances.get(androidClass);

        Node attrNode = doc.createAttribute("instance");
        attrNode.setNodeValue(instance.toString());
        attributes.setNamedItem(attrNode);

        doc.renameNode(node, node.getNamespaceURI(), androidClass);

        instances.put(androidClass, instance + 1);
    }

    private static String cleanTagName(String name) {
        name = name.replaceAll("[$@#&]", ".");
        return name.replaceAll("\\s", "");
    }
}
