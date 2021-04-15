package my.javasaml.encrypt;

import com.onelogin.saml2.util.Constants;
import com.onelogin.saml2.util.Util;
import my.javasaml.KeyConstants;
import my.javasaml.SamlUtils;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.security.*;
import java.security.cert.X509Certificate;

import static my.javasaml.KeyConstants.cert;

/**
 * This is to use pure java encrypt saml signed assertion
 */
public class MyJavaSamlEncrypt {

    public static void main(String[] args) throws Exception {
        PrivateKey privateKey = KeyConstants.privateKey;
        X509Certificate certificate = KeyConstants.cert;

        String xmlPath = "onelogin_saml_sample.xml";
        Document signedDocument = signAssertion(privateKey, certificate, xmlPath);
        boolean validateResult = Util.validateSign(signedDocument, cert, null, Constants.RSA_SHA1, "//ds:Signature");
        System.out.println("validate the signed assertion: " + validateResult);
        System.out.println("=========================");

        // *** encrypt the assertion ***
        // get the assertion from the document
        String assertionText = getAssertionFromDoc(signedDocument);

        // create an intermediate key
        Key intermediateKey = CipherUtil.createIntermediateKey();

        // encrypt it with cipher intermediate key
        String encryptedAssertion = CipherUtil.encrypt(assertionText, intermediateKey);
        // optional step: decrypt the encrypted assertion
        decryptAndVerify(encryptedAssertion, intermediateKey, assertionText);

        // wrap intermediate key with public key
        String wrappedKey = CipherUtil.wrapKey(intermediateKey, certificate.getPublicKey());
        // optional step: unwrap the wrapped intermediate key to validate
        unwrapAndVerify(wrappedKey, privateKey, intermediateKey);

        // put back the encrypted assertion and the wrapped key back to saml response
        // TODO


    }

    private static void unwrapAndVerify(String wrappedKey, PrivateKey privateKey, Key intermediateKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Key unwrappedKey = CipherUtil.unwrapKey(wrappedKey, intermediateKey.getAlgorithm(), privateKey);
        System.out.println("unwrappedKey.equals(intermediateKey): " + unwrappedKey.equals(intermediateKey));
        assert unwrappedKey.equals(intermediateKey);
    }

    private static void decryptAndVerify(String encryptedText, Key intermediateKey, String plainText)
            throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException,
            InvalidAlgorithmParameterException, InvalidKeyException {
        String text = CipherUtil.decrypt(encryptedText, intermediateKey);
        // run it with argument -ea to enable assert
        System.out.println("text.equals(plainText): " + text.equals(plainText));
        assert text.equals(plainText) : "not equal after encryption and decryption";
    }

    protected static String getAssertionFromDoc(Document signedDocument) throws TransformerException, XPathExpressionException {
        Node assertionNode = SamlUtils.findFirstNode(signedDocument, "//saml:Assertion");
        return SamlUtils.convertNodeToString(assertionNode);
    }

    private static Document signAssertion(PrivateKey privateKey, X509Certificate certificate, String xmlPath) throws IOException, XPathExpressionException, ParserConfigurationException, XMLSecurityException, TransformerException {
        Document document = SamlUtils.readDocument(xmlPath);
        String signedText = SamlUtils.signAssertion(document, privateKey, certificate);

        System.out.println("signed the SAML assertion\n" + signedText);
        System.out.println("\n=========================");

        Document signedDocument = Util.loadXML(signedText);
        return signedDocument;
    }

}
