package com.tradeSecrets;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Random;

import static com.tradeSecrets.AudioIOUtilities.readToByteArray;

public class PerformanceMetrics {

    public static final String LSB_RAW = "C:\\Users\\Edwin\\OneDrive\\2020\\Control\\Raw\\lsb.wav";
    public static final String DSSS_RAW = "C:\\Users\\Edwin\\OneDrive\\2020\\Control\\Raw\\dsss.wav";
    public static final String PhASE_RAW = "C:\\Users\\Edwin\\OneDrive\\2020\\Control\\Raw\\phase.wav";
    public static final String ECHO_RAW = "C:\\Users\\Edwin\\OneDrive\\2020\\Control\\Raw\\echoHidding.wav";
    public static final String LSB = "C:\\Users\\Edwin\\OneDrive\\2020\\Control\\Encoded\\lsb.wav";
    public static final String DSSS = "C:\\Users\\Edwin\\OneDrive\\2020\\Control\\Encoded\\DSSS.wav";
    public static final String PhASE = "C:\\Users\\Edwin\\OneDrive\\2020\\Control\\Encoded\\phase.wav";
    public static final String ECHO = "C:\\Users\\Edwin\\OneDrive\\2020\\Control\\Encoded\\singleKernal.wav";


    public static void psnr(String raw, String embedded) {
        try {
            FormattedAudioInput inputStream = readToByteArray(new File(raw));
            FormattedAudioInput extraStream = readToByteArray(new File(embedded));
            //MessageText text = new MessageText();
            AudioIOUtilities.mse(inputStream,extraStream);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void cipherTextTest(ArrayList<Boolean> cypherText,EncryptionTool encryptionTool) {

        Random random = new Random();
        int min = 2;
        int max = ByteUtil.extractHeader(cypherText)+2;
        System.out.println("Cipher Test");
        String message = decrypt(cypherText,encryptionTool);
        ArrayList<Boolean> copyText;
        int index = 0;
        int correct = 0;
        int incorrect = 0;
        int errorNumber = 0;
        for(int i = 0;i<10000;i++) {
            copyText= new ArrayList<>(cypherText);
            System.out.println(decrypt(copyText,encryptionTool));
            System.out.println(random.nextInt(max-min)+2);
            index = random.nextInt((max-min)*8)+15;
            copyText.set(index,!copyText.get(index));
            try {
                String extractedText = decrypt(copyText,encryptionTool);
                System.out.println(extractedText);
                if(extractedText.equalsIgnoreCase(message)) {

                    correct++;
                }else {
                   errorNumber++;
                }

            }catch (Exception e) {
                incorrect++;
            }



        }
        System.out.println("Bit error results");
        System.out.println(correct);
        System.out.println(incorrect);
        System.out.println(errorNumber);


    }

    public static String decrypt(ArrayList<Boolean> cypherText,EncryptionTool encryptionTool) {
        byte[] bytes = ByteUtil.binaryListToByteArray(cypherText);
        String result =  new String(bytes, StandardCharsets.UTF_8);
        String encryptedRetrieved = result.substring(2,ByteUtil.extractHeader(cypherText)+2);

        String decryptedMessage = encryptionTool.decrypt(encryptedRetrieved);
        return decryptedMessage;
    }




}
