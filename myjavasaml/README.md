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
