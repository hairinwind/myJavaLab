## This project is related to sign the SAML

## pure java signing
MyJavaSaml is using pure java to sign the assertion

## com.onelogin.saml2.util.Util
SamlUtils is using com.onelogin.saml2.util.Util to sign the assertion.

## sign - encrypt - sign
MyJavaSamlEncrypt is to sign assertion, then encrypt assertion, then sign the full saml response.  
the encryption is using java cipher.  

regarding the block cipher, iv
https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation

## SamlUtils provides the encrypt and decrypt function
example could be found in unit test SamlUtilsTest.encryptDocument()

## using the DOM API of Apache Santuario - XML Security for Java for XML Encryption 
https://github.com/coheigea/testcases/blob/master/apache/santuario/santuario-xml-encryption/src/test/java/org/apache/coheigea/santuario/xmlencryption/EncryptionDOMTest.java  
https://stackoverflow.com/questions/57261251/examples-or-tutorials-about-santuario-java  
http://www.macroprogramming.com/apache-sanctuary-cryptography-and-xml-signature-in-java/  



