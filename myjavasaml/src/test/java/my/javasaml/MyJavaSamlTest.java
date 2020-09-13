package my.javasaml;

import static my.javasaml.KeyConstants.cert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Base64;

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
//		System.out.println(myJavaSignature);
		Node myJavaDigest = SamlUtils.findFirstNode(signedDocument, "//ds:DigestValue");
		System.out.println(myJavaDigest.getTextContent());
		
		Document document1 = SamlUtils.readDocument(fileName);
		String signedText = SamlUtils.signAssertion(document1, KeyConstants.privateKey, KeyConstants.cert);
		assertTrue(Util.validateSign(Util.loadXML(signedText), cert, null, Constants.RSA_SHA1, "//ds:Signature"));
//		System.out.println("signed the SAML assertion:" + signedText);
		Node signatureNode2 = SamlUtils.findFirstNode(Util.loadXML(signedText), "//ds:SignatureValue");
		String javaSamlSignature = signatureNode2.getTextContent();
//		System.out.println("\n" + signatureNode2.getTextContent());
		Node samlUtilsDigest = SamlUtils.findFirstNode(Util.loadXML(signedText), "//ds:DigestValue");
		System.out.println(samlUtilsDigest.getTextContent());
		
		assertEquals(javaSamlSignature, myJavaSignature);
	}
	
	@Test
	public void testMySign() throws Exception {
		String fileName = "onelogin_saml_sample.xml";
		Document document = SamlUtils.readDocument(fileName);
		
		Document signedDocument = MyJavaSaml.signAssertion(document, KeyConstants.privateKey, KeyConstants.cert);
		assertTrue(Util.validateSign(signedDocument, cert, null, Constants.RSA_SHA1, "//ds:Signature"));
//		System.out.println("myJava signed doc:\n" + Util.convertDocumentToString(signedDocument, true));
		
		Node signatureNode = SamlUtils.findFirstNode(signedDocument, "//ds:SignatureValue");
		String myJavaSignature = signatureNode.getTextContent();
		System.out.println(myJavaSignature);
		
		Node signedInfoNode = SamlUtils.findFirstNode(signedDocument, "//ds:SignedInfo");
		System.out.println("signedInfoNode:\n" + SamlUtils.convertNodeToString(signedInfoNode));
		
		String canonicalizeSignedInfo = SamlUtils.canonicalize(signedInfoNode);
		System.out.println("canonicalizeSignedInfo:\n" + canonicalizeSignedInfo);
		
		byte[] sha1 = Base64.getEncoder().encode(SamlUtils.toSHA1(canonicalizeSignedInfo.getBytes()));
		byte[] signatureBytes = RSAUtils.encryptWithPrivateKey(new String(sha1), KeyConstants.privateKey);
		String signature = new String(Base64.getEncoder().encode(signatureBytes));
		System.out.println("Signature: " + new String(signature));
	}

}
