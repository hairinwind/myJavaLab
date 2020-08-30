package my.javasaml;

import static my.javasaml.KeyConstants.cert;
import static my.javasaml.KeyConstants.fingerprint;
import static my.javasaml.KeyConstants.privateKey;

import java.io.File;
import java.io.IOException;
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

import org.apache.commons.io.FileUtils;
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

public class MyJavaSaml {
	public static void main(String[] args) throws Exception {
//		String fileName = "saml_before_sign.xml";
		String fileName = "onelogin_saml_sample.xml";
		Document document = readDocument(fileName);

//		// signs the response
//		Node assertionNode = findFirstNode(document, "//saml:Assertion");
//		
//		String canonicalizedAssertion = canonicalize(assertionNode);
//		System.out.println("\ncanonicalizedAssertion:\n" + canonicalizedAssertion + "\n");
//		
//		Document signedDocument = addSign(assertionNode, privateKey, cert, null);
//		String signedDocumentText = Util.convertDocumentToString(signedDocument, true);
//		System.out.println("signedResponse: " + signedDocumentText);
		
		String signedDocumentText = SamlUtils.signAssertion(document, privateKey, cert);
		System.out.println("signedResponse: \n" + signedDocumentText);
        
		//load signed
		Document signedDocument = Util.loadXML(signedDocumentText);
		boolean validateResult = Util.validateSign(signedDocument, cert, fingerprint, Constants.RSA_SHA1, "//ds:Signature"); ///Response/ns2:Assertion/ds:Signature
		System.out.println("\nvalidate result: " + validateResult);
	}

	/**
	 * read Xml Document from classpath 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static Document readDocument(String fileName) throws IOException {
		String xmlPath = MyJavaSaml.class.getClassLoader().getResource(fileName).getFile();
		String samlXml = FileUtils.readFileToString(new File(xmlPath), "UTF-8");
		Document document = Util.loadXML(samlXml);
		return document;
	}
	
	public static Node findFirstNode(Document document, String xpath) throws XPathExpressionException {
		NodeList nodes = Util.query(document, xpath);
		if (nodes.getLength() > 0) {
			return nodes.item(0);
		} else {
			throw new RuntimeException("node not found by " + xpath);
		}
	}
	
	public static Document addSign(Node node, PrivateKey key, X509Certificate certificate, String signAlgorithm) throws ParserConfigurationException, XPathExpressionException, XMLSecurityException, TransformerFactoryConfigurationError, TransformerException {
		// Check arguments.
		if (node == null) {
			throw new IllegalArgumentException("Provided node was null");
		}

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	  	dbf.setNamespaceAware(true);
		Document doc = dbf.newDocumentBuilder().newDocument();
		Node newNode = doc.importNode(node, true);
		doc.appendChild(newNode);

		return addSign(doc, key, certificate, signAlgorithm);
	}
	
	public static Document addSign(Document document, PrivateKey key, X509Certificate certificate, String signAlgorithm) throws XMLSecurityException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException {
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

		String c14nMethod = Constants.C14NEXC_WC;

		// Signature object
		XMLSignature sig = new XMLSignature(document, null, signAlgorithm, c14nMethod);

		// Including the signature into the document before sign, because
		// this is an envelop signature
		Element root = document.getDocumentElement();
		document.setXmlStandalone(false);		
		
		NodeList assertionNodes = root.getElementsByTagName("ns2:Assertion");
		Node assertionNode = root; 
		if (assertionNodes.getLength() > 0) {
			assertionNode = assertionNodes.item(0);
		}
		
		// If Issuer, locate Signature after Issuer, Otherwise as first child.
		NodeList subjectNodes = Util.query(document, "//saml:Subject", null);
		if (subjectNodes.getLength() > 0) {
			Node subject =  subjectNodes.item(0);
			
//						System.out.println("issue node: " + convertNodeToString(issuer));
			
			assertionNode.insertBefore(sig.getElement(), subject);
//						root.insertBefore(sig.getElement(), subject);
			
//						System.out.println("root after insert: " + convertNodeToString(root));
//						System.out.println("***");
		} else {
			root.insertBefore(sig.getElement(), root.getFirstChild());
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

		// return Util.convertDocumentToString(document, true);
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
