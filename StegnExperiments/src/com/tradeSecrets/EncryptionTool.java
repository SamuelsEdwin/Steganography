package com.tradeSecrets;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptionTool {

    private String secret;
    private EncryptionUtil encryptionUtil;

    public EncryptionTool() {
        try {
            SecretKey secretKey = EncryptionUtil.generateSecretKey();


            this.secret = new String(secretKey.getEncoded(), StandardCharsets.UTF_8);
            encryptionUtil = new EncryptionUtil();




        }catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    public String encrypt(String message) {

        return encryptionUtil.encrypt(message,secret);

    }

    public String decrypt(String encryptedMessage) {

        return encryptionUtil.decrypt(encryptedMessage,secret);
    }

    public static void encryptionTest(String message) {


        try {
            SecretKey secretKey = EncryptionUtil.generateSecretKey();

            String s = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            String s1 = new String(secretKey.getEncoded(), StandardCharsets.UTF_8);
            System.out.println(s);

            EncryptionUtil util = new EncryptionUtil();
            EncryptionUtil util2 = new EncryptionUtil();

            util.setKey(s1);


            String encryptedMessage = util.encrypt(message,s1);
            System.out.println("Encrypted message\n");
            System.out.println(encryptedMessage);

            System.out.println("decrypted message\n");
            util2.setKey(s1);
            String decrypt = util2.decrypt(encryptedMessage,s1);

            System.out.println(decrypt);



        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
