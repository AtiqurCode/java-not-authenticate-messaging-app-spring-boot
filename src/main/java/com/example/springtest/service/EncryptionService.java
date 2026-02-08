package com.example.springtest.service;

import com.example.springtest.config.EncryptionConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class EncryptionService {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY_ALGORITHM = "AES";
    private static final int IV_SIZE = 16; // 128 bits

    private final EncryptionConfig encryptionConfig;

    /**
     * Encrypt a message using AES-256-CBC
     * Format: IV (16 bytes) + encrypted data
     * Return: Base64 encoded string
     */
    public String encrypt(String plainText) {
        try {
            if (plainText == null || plainText.isEmpty()) {
                return plainText;
            }

            // Generate random IV
            byte[] iv = new byte[IV_SIZE];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // Create key from secret
            SecretKeySpec keySpec = createKey();

            // Encrypt
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            // Combine IV + encrypted data
            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

            // Base64 encode
            return Base64.encodeBase64String(combined);

        } catch (Exception e) {
            log.error("Error encrypting message", e);
            throw new RuntimeException("Failed to encrypt message", e);
        }
    }

    /**
     * Decrypt a message using AES-256-CBC
     * Input: Base64 encoded string (IV + encrypted data)
     * Return: Plain text string
     */
    public String decrypt(String encryptedText) {
        try {
            if (encryptedText == null || encryptedText.isEmpty()) {
                return encryptedText;
            }

            // Base64 decode
            byte[] combined = Base64.decodeBase64(encryptedText);

            // Extract IV
            byte[] iv = Arrays.copyOfRange(combined, 0, IV_SIZE);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // Extract encrypted data
            byte[] encrypted = Arrays.copyOfRange(combined, IV_SIZE, combined.length);

            // Create key from secret
            SecretKeySpec keySpec = createKey();

            // Decrypt
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decrypted = cipher.doFinal(encrypted);

            return new String(decrypted, StandardCharsets.UTF_8);

        } catch (Exception e) {
            log.error("Error decrypting message", e);
            throw new RuntimeException("Failed to decrypt message", e);
        }
    }

    /**
     * Create a 256-bit key from the secret string using SHA-256
     */
    private SecretKeySpec createKey() throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = sha.digest(encryptionConfig.getSecretKey().getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(key, KEY_ALGORITHM);
    }
}
