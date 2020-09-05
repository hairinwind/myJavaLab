package my.javasaml;

import static my.javasaml.KeyConstants.cert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.onelogin.saml2.util.Constants;
import com.onelogin.saml2.util.Util;

public class MyJavaSamlTest {

	@Test
	public void testSignAssertion() throws Exception {
		String fileName = "onelogin_saml_sample2.xml";
		Document document = SamlUtils.readDocument(fileName);
		
		Document signedDocument = MyJavaSaml.signAssertion(document, KeyConstants.privateKey, KeyConstants.cert);
		
		boolean validateResult = Util.validateSign(signedDocument, cert, null, Constants.RSA_SHA1, "//ds:Signature");
		System.out.println(Util.convertDocumentToString(signedDocument));
		System.out.println(validateResult);
		assertTrue(validateResult);
	}
	
	@Test
	public void compareWithSamlUtils() throws Exception {
		String fileName = "onelogin_saml_sample.xml";
		Document document = SamlUtils.readDocument(fileName);
		
		Document signedDocument = MyJavaSaml.signAssertion(document, KeyConstants.privateKey, KeyConstants.cert);
		assertTrue(Util.validateSign(signedDocument, cert, null, Constants.RSA_SHA1, "//ds:Signature"));
//		System.out.println("myJava signed doc:\n" + Util.convertDocumentToString(signedDocument, true));
		
		Node signatureNode = SamlUtils.findFirstNode(signedDocument, "//ds:SignatureValue");
		String myJavaSignature = signatureNode.getTextContent();
		System.out.println(signatureNode.getTextContent());
		
		Document document1 = SamlUtils.readDocument(fileName);
		String signedText = SamlUtils.signAssertion(document1, KeyConstants.privateKey, KeyConstants.cert);
		assertTrue(Util.validateSign(Util.loadXML(signedText), cert, null, Constants.RSA_SHA1, "//ds:Signature"));
//		System.out.println("signed the SAML assertion:" + signedText);
		Node signatureNode2 = SamlUtils.findFirstNode(signedDocument, "//ds:SignatureValue");
		String javaSamlSignature = signatureNode2.getTextContent();
		System.out.println("\n" + signatureNode2.getTextContent());
		
		assertEquals(javaSamlSignature, myJavaSignature);
	}

}
