package com.tradeSecrets;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.io.UnsupportedEncodingException;

import java.security.NoSuchAlgorithmException;

import java.util.Base64;

import javax.crypto.Cipher;


public class EncryptionUtil {

    private static SecretKeySpec secretKey;
    private static byte[] key;
    private static String SHA_256 = "SHA-1";
    private static String ENCRYPTION_ALGORITHM = "AES";
    private static String ENCODING = "UTF-8";
    public  void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes(ENCODING);
            sha = MessageDigest.getInstance(SHA_256);

            key = sha.digest(key);

            key = Arrays.copyOf(key,16);//16
            secretKey = new SecretKeySpec(key,ENCRYPTION_ALGORITHM);

            //secretKey = generateSecretKey();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static SecretKey generateSecretKey() throws NoSuchAlgorithmException {

        KeyGenerator keyGen = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
        SecureRandom random = new SecureRandom(); // cryptograph. secure random
        keyGen.init(random);
        return keyGen.generateKey();

    }

    public  String encrypt(String strToEncrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public  String decrypt(String strToDecrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

}
