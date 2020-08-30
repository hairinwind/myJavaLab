package my.javasaml;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.onelogin.saml2.util.Util;

public class DigestUtils {

	public static void main(String[] args) throws Exception {
		String xmlPath = "saml_before_sign.xml";
		Document document = MyJavaSaml.readDocument(xmlPath);
		Node assertionNode = MyJavaSaml.findFirstNode(document, "//saml:Assertion");
		
		System.out.println("\nDigest\n" + createDigestValue(assertionNode));
	}
	
	public static String createDigestValue(Node document) throws TransformerFactoryConfigurationError, TransformerException, InvalidCanonicalizerException, CanonicalizationException, ParserConfigurationException, IOException, SAXException, NoSuchAlgorithmException {
		// create the transformer in order to transform the document from
        // DOM Source as a JAVA document class, into a character stream (StreamResult) of
        // type String writer, in order to be converted to a string later on
//        TransformerFactory tf = new TransformerFactoryImpl(); //net.sf.saxon.
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        // create the string writer and transform the document to a character stream
        StringWriter sw = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(sw));
        String documentAsString = sw.toString();

        // initialize the XML security object, which is necessary to run the apache canonicalization
//        com.sun.org.apache.xml.internal.security.Init.init();
        org.apache.xml.security.Init.init();

        // canonicalize the document to a byte array and convert it to string
        Canonicalizer canon = Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS); //ALGO_ID_C14N_EXCL_WITH_COMMENTS
        byte canonXmlBytes[] = canon.canonicalize(documentAsString.getBytes());
        String canonXmlString = new String(canonXmlBytes);
        
        System.out.println("canonXmlString\n" + canonXmlString);

        // get instance of the message digest based on the SHA-1 hashing algorithm
        MessageDigest digest = MessageDigest.getInstance("SHA-1");

        // call the digest method passing the byte stream on the text, this directly updates the message
        // being digested and perform the hashing
        byte[] hash = digest.digest(canonXmlString.getBytes(StandardCharsets.UTF_8));

        // encode the endresult byte hash
        byte[] encodedBytes = Base64.encodeBase64(hash);

        return new String(encodedBytes);
	}
}