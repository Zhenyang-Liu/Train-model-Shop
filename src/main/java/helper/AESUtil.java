package helper;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

import java.security.NoSuchAlgorithmException;


public class AESUtil {

    /**
     * Encrypts data using AES encryption.
     *
     * This method takes a string of data and an AES key string, encrypts the data using AES encryption, 
     * and then returns the encrypted data in Base64 encoded format.
     *
     * @param data The data to be encrypted.
     * @param keyString The AES key string used for encryption.
     * @return The encrypted data in Base64 encoded format.
     * @throws Exception if an error occurs during the encryption process.
     */
    public static String encrypt(String data, String keyString) throws Exception {
        Key key = new SecretKeySpec(keyString.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * Decrypts AES encrypted data.
     *
     * This method takes a string of encrypted data in Base64 format and an AES key string, 
     * decrypts the data using AES decryption, and returns the original data as a string.
     *
     * @param encryptedData The encrypted data in Base64 format to be decrypted.
     * @param keyString The AES key string used for decryption.
     * @return The original decrypted data as a string.
     * @throws Exception if an error occurs during the decryption process.
     */
    public static String decrypt(String encryptedData, String keyString) throws Exception {
        Key key = new SecretKeySpec(keyString.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(original);
    }

    /**
     * Generates an AES encryption key.
     *
     * This method creates a new AES key using the AES algorithm. The key is generated with a length of 128 bits
     * and is returned as a Base64 encoded string.
     *
     * @return The generated AES key as a Base64 encoded string.
     * @throws NoSuchAlgorithmException if the AES algorithm is not available.
     */
    public static String generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);

        SecretKey secretKey = keyGen.generateKey();
    
        byte[] keyBytes = secretKey.getEncoded();
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
    
        return base64Key;
    }
}
