package my.javasaml;

import static my.javasaml.KeyConstants.cert;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.onelogin.saml2.util.Constants;
import com.onelogin.saml2.util.Util;

public class MyJavaSaml {
	public static void main(String[] args) throws Exception {
//		String fileName = "saml_before_sign.xml";
		String fileName = "onelogin_saml_sample2.xml";
		Document document = SamlUtils.readDocument(fileName);
		
		Document signedDocument = signAssertion(document, KeyConstants.privateKey, KeyConstants.cert);
		
		boolean validateResult = Util.validateSign(signedDocument, cert, null, Constants.RSA_SHA1, "//ds:Signature");
		System.out.println(Util.convertDocumentToString(signedDocument));
		System.out.println(validateResult);
	}

	public static Document signAssertion(Document doc, PrivateKey privateKey, X509Certificate publicCertificate) throws Exception {
		// Instance main XML Signature Toolkit.
		XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
		XPathFactory xPathfactory = XPathFactory.newInstance();

		// Retreive Assertion Node to be signed.
		XPath xpath = xPathfactory.newXPath();
		XPathExpression exprAssertion = xpath.compile("//*[local-name()='Response']//*[local-name()='Assertion']");
		Element assertionNode = (Element) exprAssertion.evaluate(doc, XPathConstants.NODE);
		// Must mark ID Atrribute as XML ID to avoid BUG in Java 1.7.25.
		assertionNode.setIdAttribute("ID", true);

		// Retreive Assertion ID because it is used in the URI attribute of the
		// signature.
		XPathExpression exprAssertionID = xpath
				.compile("//*[local-name()='Response']//*[local-name()='Assertion']//@ID");
		String assertionID = (String) exprAssertionID.evaluate(doc, XPathConstants.STRING);

		// Retreive Subject Node because the signature will be inserted before.
		XPathExpression exprAssertionSubject = xpath
				.compile("//*[local-name()='Response']//*[local-name()='Assertion']//*[local-name()='Subject']");
		Node insertionNode = (Node) exprAssertionSubject.evaluate(doc, XPathConstants.NODE);

		// Create the DOMSignContext by specifying the signing informations: Private
		// Key, Node to be signed, Where to insert the Signature.
		DOMSignContext dsc = new DOMSignContext(privateKey, assertionNode, insertionNode);
		dsc.setDefaultNamespacePrefix("ds");

		// Create a CanonicalizationMethod which specify how the XML will be
		// canonicalized before signed.
		CanonicalizationMethod canonicalizationMethod = fac.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE,
				(C14NMethodParameterSpec) null);
		// Create a SignatureMethod which specify how the XML will be signed.
		SignatureMethod signatureMethod = fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null);

		// Create an Array of Transform, add it one Transform which specify the
		// Signature ENVELOPED method.
		List<Transform> transformList = new ArrayList<Transform>(1);
		transformList.add(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null));	
		//add xml-exc-c14n transform
		transformList.add(fac.newTransform("http://www.w3.org/2001/10/xml-exc-c14n#", (C14NMethodParameterSpec) null));
		
		// Create a Reference which contain: An URI to the Assertion ID, the Digest
		// Method and the Transform List which specify the Signature ENVELOPED method.
		Reference reference = fac.newReference("#" + assertionID, fac.newDigestMethod(DigestMethod.SHA1, null),
				transformList, null, null);
		List<Reference> referenceList = Collections.singletonList(reference);
		// Create a SignedInfo with the pre-specified: Canonicalization Method,
		// Signature Method and List of References.
		SignedInfo si = fac.newSignedInfo(canonicalizationMethod, signatureMethod, referenceList);

		// Create a new KeyInfo and add it the Public Certificate.
		KeyInfoFactory kif = fac.getKeyInfoFactory();
		List x509Content = new ArrayList();
		x509Content.add(publicCertificate);
		X509Data xd = kif.newX509Data(x509Content);
		KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));

		// Create a new XML Signature with the pre-created : Signed Info & Key Info
		XMLSignature signature = fac.newXMLSignature(si, ki);
		signature.sign(dsc);

		// Return the Signed Assertion.
		return doc;
	}
}
