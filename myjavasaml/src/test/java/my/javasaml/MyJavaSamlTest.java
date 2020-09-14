package my.javasaml;

import static my.javasaml.KeyConstants.cert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Base64;

import javax.xml.xpath.XPathExpressionException;

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
		
		Document document1 = SamlUtils.readDocument(fileName);
		String signedText = SamlUtils.signAssertion(document1, KeyConstants.privateKey, KeyConstants.cert);
		Document signedDocument1 = Util.loadXML(signedText);
//		assertTrue(Util.validateSign(signedDocument1, cert, null, Constants.RSA_SHA1, "//ds:Signature"));
		
		String fileName2= "onelogin_saml_sample2.xml";
		Document document2 = SamlUtils.readDocument(fileName2);
		Document signedDocument2 = MyJavaSaml.signAssertion(document2, KeyConstants.privateKey, KeyConstants.cert);
		assertTrue(Util.validateSign(signedDocument2, cert, null, Constants.RSA_SHA1, "//ds:Signature"));
		
		printDigestAndSignature(signedDocument, signedDocument1, signedDocument2);
		
		printSignedDocument(signedDocument, signedDocument1);
		
//		assertEquals(javaSamlSignature, myJavaSignature);
	}

	private void printSignedDocument(Document... signedDocuments) {
		// TODO Auto-generated method stub
		for (Document doc : signedDocuments) {
			System.out.println(Util.convertDocumentToString(doc));
			System.out.println("=====");
		}
	}

	private void printDigestAndSignature(Document... signedDocuments) throws XPathExpressionException {
		System.out.println("=== Digest === ");
		for (Document doc : signedDocuments) {
			System.out.println(extractDigestValue(doc));
		}
		
		System.out.println("=== Signature === ");
		for (Document doc : signedDocuments) {
			System.out.println(extractSignature(doc));
		}
	}

	private String extractDigestValue(Document signedDocument) throws XPathExpressionException {
		Node digest = SamlUtils.findFirstNode(signedDocument, "//*[local-name()='DigestValue']"); //ds:DigestValue
		return digest.getTextContent();
	}
	
	private String extractSignature(Document signedDocument) throws XPathExpressionException {
		Node signatureNode = SamlUtils.findFirstNode(signedDocument, "//*[local-name()='SignatureValue']");  //ds:SignatureValue
		String signature = signatureNode.getTextContent();
		return signature;
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
