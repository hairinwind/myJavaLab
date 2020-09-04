package my.javasaml;

import static org.junit.Assert.assertEquals;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class DigestUtilsTest {

	@Test
	public void testCreateDigestValue() throws Exception {
		String xmlPath = "saml_before_sign.xml";
		Document document = SamlUtils.readDocument(xmlPath);
		Node assertionNode = SamlUtils.findFirstNode(document, "//saml:Assertion");
		
		String digestValue = DigestUtils.createDigestValue(assertionNode);
		assertEquals("+PuUCVHDGjytJJBvt+72oxZmv5g=", digestValue);
		
		//create digest step by step
		//canonicalize the text
		String canonicalizeText = SamlUtils.canonicalize(assertionNode); 
		System.out.println("canonicalizeText\n" + canonicalizeText);
		//SHA-1 then base64 encode
		String sha1Text = new String(SamlUtils.toSHA1(canonicalizeText.getBytes())); 
		
		assertEquals(digestValue, sha1Text);
	}
	
	

}
