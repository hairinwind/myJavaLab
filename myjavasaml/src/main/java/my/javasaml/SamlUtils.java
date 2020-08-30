package my.javasaml;

import java.io.StringWriter;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.onelogin.saml2.util.Constants;
import com.onelogin.saml2.util.Util;

public class SamlUtils {
	
	/**
	 * Sign the assertion inside the SAML response
	 * @param document
	 * @param privateKey
	 * @param certificate
	 * @return
	 * @throws XPathExpressionException
	 * @throws ParserConfigurationException
	 * @throws XMLSecurityException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public static String signAssertion(Document document, PrivateKey privateKey, X509Certificate certificate) throws XPathExpressionException, ParserConfigurationException, XMLSecurityException, TransformerFactoryConfigurationError, TransformerException {
		String assertionXpath = "//saml:Assertion";
		Node assertionNode = findFirstNode(document, assertionXpath);
		if (assertionNode == null) {
			throw new RuntimeException("assertion node is not found by xpath //saml:Assertion");
		}
		System.out.println("assertion node: " + Util.convertDocumentToString(nodeToDocument(assertionNode)));
		
		Document signedDocument = addSign(assertionNode, privateKey, certificate, null);
		
		System.out.println("assertion node signed: " + Util.convertDocumentToString(signedDocument));
		
		Node signedAssertionNode = document.importNode(signedDocument.getDocumentElement(), true);
		assertionNode.getParentNode().replaceChild(signedAssertionNode, assertionNode);
		
		return Util.convertDocumentToString(document, true);
	}
	
	/**
	 * Sign the whole SAML response
	 * @param document
	 * @param privateKey
	 * @param certificate
	 * @return
	 * @throws XPathExpressionException
	 * @throws ParserConfigurationException
	 * @throws XMLSecurityException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public static String sign(Document document, PrivateKey privateKey, X509Certificate certificate) throws XPathExpressionException, ParserConfigurationException, XMLSecurityException, TransformerFactoryConfigurationError, TransformerException {		
		Document signedDocument = addSign(document, privateKey, certificate, null);
		return Util.convertDocumentToString(signedDocument, true);
	}
	
	public static Node findFirstNode(Document document, String xpath) throws XPathExpressionException {
		NodeList nodes = Util.query(document, xpath);
		if (nodes.getLength() > 0) {
			return nodes.item(0);
		} else {
			throw new RuntimeException("node not found by " + xpath);
		}
	}
	
	private static Document addSign(Node node, PrivateKey key, X509Certificate certificate, String signAlgorithm) throws ParserConfigurationException, XPathExpressionException, XMLSecurityException, TransformerFactoryConfigurationError, TransformerException {
		Document doc = nodeToDocument(node);

		return addSign(doc, key, certificate, signAlgorithm);
	}

	public static Document nodeToDocument(Node node) throws ParserConfigurationException {
		if (node == null) {
			throw new IllegalArgumentException("Provided node was null");
		}

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	  	dbf.setNamespaceAware(true);
		Document doc = dbf.newDocumentBuilder().newDocument();
		Node newNode = doc.importNode(node, true);
		doc.appendChild(newNode);
		return doc;
	}
	
	private static Document addSign(Document document, PrivateKey key, X509Certificate certificate, String signAlgorithm) throws XMLSecurityException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException {
		org.apache.xml.security.Init.init();

		// Check arguments.
		if (document == null) {
			throw new IllegalArgumentException("Provided document was null");
		}

		if (document.getDocumentElement() == null) {
			throw new IllegalArgumentException("The Xml Document has no root element.");
		}

		if (key == null) {
			throw new IllegalArgumentException("Provided key was null");
		}
		
		if (certificate == null) {
			throw new IllegalArgumentException("Provided certificate was null");
		}

		if (signAlgorithm == null || signAlgorithm.isEmpty()) {
			signAlgorithm = Constants.RSA_SHA1;
		}

		// document.normalizeDocument();
		String c14nMethod = Constants.C14NEXC; //C14NEXC_WC
		
		// Signature object
		XMLSignature sig = new XMLSignature(document, null, signAlgorithm, c14nMethod);

		// Including the signature into the document before sign, because
		// this is an envelop signature
		Element root = document.getDocumentElement();
		document.setXmlStandalone(false);
		
		// If Issuer, locate Signature after Issuer, Otherwise as first child.
		String xpathToInsertSignature = "//saml:Issuer";
		NodeList targetNodes = Util.query(document, xpathToInsertSignature, null);
		if (targetNodes.getLength() > 0) {
			Node targetNode =  targetNodes.item(0);
			root.insertBefore(sig.getElement(), targetNode.getNextSibling());
		} else {
			root.insertBefore(sig.getElement(), root.getFirstChild().getNextSibling());
		}

		String id = root.getAttribute("ID");

		String reference = id;
		if (!id.isEmpty()) {
			root.setIdAttributeNS(null, "ID", true);
			reference = "#" + id;
		}

		// Create the transform for the document
		Transforms transforms = new Transforms(document);
		transforms.addTransform(Constants.ENVSIG);
		//transforms.addTransform(Transforms.TRANSFORM_C14N_OMIT_COMMENTS);
		transforms.addTransform(c14nMethod);
		sig.addDocument(reference, transforms, Constants.SHA1);

		// Add the certification info
		sig.addKeyInfo(certificate);			

		// Sign the document
		sig.sign(key);

		return document;
	}
	
	public static String convertNodeToString(Node node) throws TransformerFactoryConfigurationError, TransformerException {
		StringWriter writer = new StringWriter();
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.transform(new DOMSource(node), new StreamResult(writer));
		String xml = writer.toString();
		return xml;
	}
	
	public static String canonicalize(Node xmlNode) throws InvalidCanonicalizerException, CanonicalizationException {
		org.apache.xml.security.Init.init(); // 
		Canonicalizer canon = Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_OMIT_COMMENTS); //Canonicalizer.ALGO_ID_C14N_OMIT_COMMENTS
		byte canonXmlBytes[] = canon.canonicalizeSubtree(xmlNode);
		String canonXmlString = new String(canonXmlBytes);
		return canonXmlString;
	}
}
