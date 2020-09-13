package my.javasaml;

import static my.javasaml.KeyConstants.privateKeyText;
import static my.javasaml.KeyConstants.publicKey;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

public class RSAUtils {

	public static PublicKey getPublicKey(String base64PublicKey) {
		PublicKey publicKey = null;
		try {
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			publicKey = keyFactory.generatePublic(keySpec);
			return publicKey;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return publicKey;
	}

	public static PrivateKey getPrivateKey(String base64PrivateKey) {
		PrivateKey privateKey = null;
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		try {
			privateKey = keyFactory.generatePrivate(keySpec);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return privateKey;
	}

	public static byte[] encrypt(String data, String publicKey) throws BadPaddingException, IllegalBlockSizeException,
			InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
		return cipher.doFinal(data.getBytes());
	}
	
	public static byte[] encrypt(byte[] data, PublicKey publicKey) throws BadPaddingException, IllegalBlockSizeException,
		InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	public static byte[] encrypt(String data, PublicKey publicKey) throws BadPaddingException,
			IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(data.getBytes());
	}
	
	public static byte[] encryptWithPrivateKey(String data, PrivateKey privateKey) throws BadPaddingException,
			IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		return cipher.doFinal(data.getBytes());
	}
	
	public static String decryptWithPublicKey(byte[] data, PublicKey publicKey) throws NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		return new String(cipher.doFinal(data));
	}

	public static String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return new String(cipher.doFinal(data));
	}

	public static String decrypt(String data, String base64PrivateKey) throws IllegalBlockSizeException,
			InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		return decrypt(Base64.getDecoder().decode(data.getBytes()), getPrivateKey(base64PrivateKey));
	}

	public static String decrypt1(String data, String base64PrivateKey) throws IllegalBlockSizeException,
			InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		return decrypt(data.getBytes(), getPrivateKey(base64PrivateKey));
	}

	public static PublicKey getPublicKeyFromX509(InputStream x509InputStream)
			throws FileNotFoundException, CertificateException {
		X509Certificate x509 = X509Certificate.getInstance(x509InputStream);
		return x509.getPublicKey();
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// The RSA encryption can only done on 117 characters on 512bit
		try {
			String encryptedString = Base64.getEncoder().encodeToString(encrypt("abc", publicKey));
			System.out.println(encryptedString);
			String decryptedString = RSAUtils.decrypt(encryptedString, privateKeyText);
			System.out.println(decryptedString);
		} catch (NoSuchAlgorithmException e) {
			System.err.println(e.getMessage());
		}
		
		//decrypt xml signature
		String signatureValue = "XcNTYoxQCKXQKLzuMcU7bwhO7sb35IF5ZY6hN/WF5x7iLsc1WLK/CiONiOQMk7Lw8UZsO2N1rIOAjPCwU5UuctHZuHqtbJvWEe6QbbNHA5u7eM2fd4h3SrPfghLjMvw93x/qJU4/JADDIm2ILhJY6g7e4qcN2sVsXa6l2AddP5I=";
		String decryptedString = RSAUtils.decrypt(signatureValue, privateKeyText);
		System.out.println(decryptedString);
	}
}
