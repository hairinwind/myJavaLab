package my.javasaml;

import static org.junit.Assert.assertEquals;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.Test;

public class RSAUtilsTest {

	@Test
	public void testEncryptWithPrivateKey() throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException {
		String data = "abc";
		byte[] encrypted = RSAUtils.encryptWithPrivateKey(data , KeyConstants.privateKey);
		String base64EncryptedText = Base64.getEncoder().encodeToString(encrypted);
		System.out.println(base64EncryptedText);
		//when encrypted with private key, the encrypted text is static
		String expectedEncryptedText = "HSVI52wVPUPMDI9INoimlD2O1A5Y8nyZLTuM1+OquWgVHcx+xsUKadWdV1Dm6HjAnTvOq7hdjDgPeSVBS7KpJtYfKmtEccuQDznZ9JJwiy/BQ9mjhPI433udXYrj6RjMD5mrEFP3+Cssl9hsHbvMryaXvenqRCzz0BvhyvYNyPE=";
		assertEquals(expectedEncryptedText, base64EncryptedText);
		
		byte[] data1 = Base64.getDecoder().decode(base64EncryptedText);
		String decryptedText = RSAUtils.decryptWithPublicKey(data1 , KeyConstants.publicKey);
		System.out.println("decrypted text: " + decryptedText);
		assertEquals(data, decryptedText);
	}
	
	@Test
	public void testEncryptWithPublicKey() throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException {
		String data = "abc";
		byte[] encrypted = RSAUtils.encrypt(data , KeyConstants.publicKey);
		String base64EncryptedText = Base64.getEncoder().encodeToString(encrypted);
		//once it is encrypted by public key, the encrypted text is changed each time
		
		byte[] data1 = Base64.getDecoder().decode(base64EncryptedText);
		String decryptedText = RSAUtils.decrypt(data1 , KeyConstants.privateKey);
		System.out.println("decrypted text: " + decryptedText);
		assertEquals(data, decryptedText);
	}
	
	@Test
	public void testDecryptSamlSignatureWithPublicKey() throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
		String encryption = "cIvNXikZ+aeyC2tcc470KmopXfifAhlAcZ4ArdLCdTOJ1WOpNs1vPwNBeIVm5JWS4V1e7EQkyuZVsaBcThWobfoYPqYVU9wUaF554uCofDyC+Nvh6lOhqTLvS3q6ZOWaoIPlIG1oZZjQ5xQvV9IHbT3q+hg8M7K5rHiCHiktUNg=";
		byte[] data1 = Base64.getDecoder().decode(encryption);
		String decryptedText = RSAUtils.decryptWithPublicKey(data1 , KeyConstants.publicKey);
		System.out.println("decrypted text: " + decryptedText);
	}

}
