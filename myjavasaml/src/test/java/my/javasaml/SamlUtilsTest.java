package my.javasaml;

import static my.javasaml.KeyConstants.cert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
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
		String xmlPath = "saml_before_sign.xml";
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
		assertEquals("ns2:Assertion", signatureNode.getParentNode().getNodeName());
		assertEquals("Response", signatureNode.getParentNode().getParentNode().getNodeName());
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

}
