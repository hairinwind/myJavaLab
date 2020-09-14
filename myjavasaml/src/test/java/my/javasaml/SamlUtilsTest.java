package my.javasaml;

import static my.javasaml.KeyConstants.cert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.onelogin.saml2.util.Constants;
import com.onelogin.saml2.util.Util;

public class SamlUtilsTest {

	private PrivateKey privateKey;
	private X509Certificate certificate;
	
	@Before
	public void setup() {
		privateKey = KeyConstants.privateKey;
		certificate = KeyConstants.cert;
	}

	@Test
	public void testSignAssertion() throws Exception {
//		String xmlPath = "saml_before_sign.xml";
		String xmlPath = "onelogin_saml_sample.xml";
		Document document = SamlUtils.readDocument(xmlPath);
		String signedText = SamlUtils.signAssertion(document, privateKey, certificate);
		
		System.out.println("signed the SAML assertion:" + signedText);
		
		Document signedDocument = Util.loadXML(signedText);
		boolean validateResult = Util.validateSign(signedDocument, cert, null, Constants.RSA_SHA1, "//ds:Signature");
		
		assertTrue(validateResult);
		NodeList nodes = Util.query(document, "//ds:Signature");
		assertEquals(1, nodes.getLength());
		
		Node signatureNode = SamlUtils.findFirstNode(signedDocument, "//ds:Signature");
		assertNotNull(signatureNode);
		assertEquals("Assertion", signatureNode.getParentNode().getLocalName());
		assertEquals("Response", signatureNode.getParentNode().getParentNode().getLocalName());
	}

	@Test
	public void testSign() throws Exception {
		String xmlPath = "saml_before_sign.xml";
		Document document = SamlUtils.readDocument(xmlPath);
		String signedText = SamlUtils.sign(document, privateKey, certificate);
		
		System.out.println("signed the whole SAML:" + signedText);
		
		Document signedDocument = Util.loadXML(signedText);
		boolean validateResult = Util.validateSign(signedDocument, cert, null, Constants.RSA_SHA1, "//ds:Signature");
		
		assertTrue(validateResult);
		Node signatureNode = SamlUtils.findFirstNode(signedDocument, "//ds:Signature");
		assertNotNull(signatureNode);
		assertEquals("Response", signatureNode.getParentNode().getNodeName());
	}
	
	@Test
	public void testFindFirstNode() throws IOException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException {
		String xmlPath = "onelogin_saml_sample2.xml";
		Document document = SamlUtils.readDocument(xmlPath);
		
		Node assertionNode = SamlUtils.findFirstNode(document, "//saml:Assertion");
		System.out.println(SamlUtils.convertNodeToString(assertionNode));
		
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression exprAssertion = xpath.compile("//*[local-name()='Response']//*[local-name()='Assertion']");
		Element assertionNodeByXpath = (Element) exprAssertion.evaluate(document, XPathConstants.NODE);
		System.out.println("\n" + getNodeString(assertionNodeByXpath));
		
		fail("this shall be same with the original text in xml");
	}
	
	public String getNodeString(Node node) throws TransformerFactoryConfigurationError, TransformerException {
		StringWriter writer = new StringWriter();
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		
		DOMSource xmlSource = new DOMSource(node);
		transformer.transform(xmlSource, new StreamResult(writer));
		String xml = writer.toString();
		return xml;
	}

}
