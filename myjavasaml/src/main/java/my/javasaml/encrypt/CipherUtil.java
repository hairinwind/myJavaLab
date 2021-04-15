package my.javasaml.encrypt;

import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

public class CipherUtil {

    public static Key createIntermediateKey() throws NoSuchAlgorithmException {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(128);
        return keygen.generateKey();
    }

    public static String encrypt(String assertionText, Key intermediateKey) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = generateIv();
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, intermediateKey, ivSpec);
        byte[] cipherData = cipher.doFinal(assertionText.getBytes(StandardCharsets.UTF_8));
        // merge iv with cipherData
        byte[] ivCipherData = ArrayUtils.addAll(iv, cipherData);
        return new String(Base64.getEncoder().encode(ivCipherData));
    }

    private static byte[] generateIv() throws NoSuchAlgorithmException {
        // here is just one way to create iv
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128, SecureRandom.getInstance("SHA1PRNG"));
        SecretKey secretKey = keyGenerator.generateKey();

        byte[] ivData = Arrays.copyOfRange(secretKey.getEncoded(),0, 16);
        return ivData;
    }

    public static String decrypt(String encryptedText, Key intermediateKey) throws NoSuchPaddingException,
            NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        byte[] cipherData = Base64.getDecoder().decode(encryptedText);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = generateIv();
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, intermediateKey, ivSpec);
        byte[] plainIvAndData = cipher.doFinal(cipherData);

        //remove the iv add in the beginning, check the encrypt method
        byte[] plainData = Arrays.copyOfRange(plainIvAndData, 16, plainIvAndData.length);
        return new String(plainData);
    }

    public static String wrapKey(Key intermediateKey, PublicKey publicKey) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(Cipher.WRAP_MODE, publicKey);
        byte[] cipherKey = cipher.wrap(intermediateKey);
        return new String(Base64.getEncoder().encode(cipherKey));
    }

    public static Key unwrapKey(String wrappedKey, String wrappedKeyAlogorithm, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        byte[] wrappedKeyData = Base64.getDecoder().decode(wrappedKey.getBytes(StandardCharsets.UTF_8));
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.UNWRAP_MODE, privateKey);
        Key unwrappedKey = cipher.unwrap(wrappedKeyData, wrappedKeyAlogorithm, Cipher.SECRET_KEY);
        return unwrappedKey;
    }
}
