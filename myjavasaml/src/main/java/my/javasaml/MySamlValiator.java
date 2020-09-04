package my.javasaml;

import static my.javasaml.KeyConstants.cert;
import static my.javasaml.KeyConstants.fingerprint;

import org.w3c.dom.Document;

import com.onelogin.saml2.util.Constants;
import com.onelogin.saml2.util.Util;

public class MySamlValiator {
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String xmlPath = "samlSignedByOneTool.xml";
		Document document = SamlUtils.readDocument(xmlPath);
		
		boolean validateResult = Util.validateSign(document, cert, fingerprint, Constants.RSA_SHA1, "//ds:Signature"); 
		System.out.println("validate result: " + validateResult);
	}

}