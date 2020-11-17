package com.tradeSecrets;


import org.junit.jupiter.api.MethodOrderer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;


public class SteganographyUtil {

    public static void leastSignificantBitEncoding(FormattedAudioInput input,String filename, String message) {

        byte[] inputBytes = input.stream.toByteArray();
        int jumps =input.format.getSampleSizeInBits()/Byte.SIZE;




        //todo test if 2
        //System.out.println(jumps);
        byte[] outputBytes = new byte[inputBytes.length];
        boolean[] binaryMessage = ByteUtil.byteArrayToBooleanBitArray(message.getBytes(StandardCharsets.UTF_8));
        int messageIndex = 0;
        //todo check how channels are encoded again
        for(int i = 0;i*jumps+1<inputBytes.length;i++) {

            outputBytes[i*jumps] = inputBytes[i*jumps];
            //sample check
            if(messageIndex<binaryMessage.length) {

                outputBytes[i*jumps+1] = ByteUtil.leastSignificantEncode(inputBytes[i * jumps+1], binaryMessage[messageIndex]);
                messageIndex++;
            }else {
                outputBytes[i*jumps+1] = inputBytes[i * jumps+1];
            }

        }

        //ByteArrayInputStream inputStream = new ByteArrayInputStream(outputBytes);
        try {
            AudioIOUtilities.writeAudioFromByteArray(outputBytes, input, filename);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }


    }

    public static void leastSignificantBitPrefixEncoding(FormattedAudioInput input,String filename, String message) {

        byte[] inputBytes = input.stream.toByteArray();
        int jumps =input.format.getSampleSizeInBits()/Byte.SIZE;




        //todo test if 2
        //System.out.println(jumps);
        byte[] outputBytes = new byte[inputBytes.length];
        boolean[] binaryMessage = ByteUtil.byteArrayToPrefixedBooleanBitArray(message.getBytes(StandardCharsets.UTF_8));
        int messageIndex = 0;
        //todo check how channels are encoded again
        for(int i = 0;i*jumps+1<inputBytes.length;i++) {

            outputBytes[i*jumps] = inputBytes[i*jumps];
            //sample check
            if(messageIndex<binaryMessage.length) {

                outputBytes[i*jumps+1] = ByteUtil.leastSignificantEncode(inputBytes[i * jumps+1], binaryMessage[messageIndex]);
                messageIndex++;
            }else {
                outputBytes[i*jumps+1] = inputBytes[i * jumps+1];
            }

        }


        try {
            AudioIOUtilities.writeAudioFromByteArray(outputBytes, input, filename);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }


    }

    public static ArrayList<Boolean> leastSignificantBitDecoding(FormattedAudioInput input) {

        byte[] inputBytes = input.stream.toByteArray();
        int jumps =input.format.getSampleSizeInBits()/Byte.SIZE;


        ArrayList<Boolean> binaryMessage = new ArrayList<>();

        //todo check how channels are encoded again
        for(int i = 0;i*jumps+1<inputBytes.length;i++) {
            binaryMessage.add(ByteUtil.getBit(0,inputBytes[i*jumps+1]));
        }

        return binaryMessage;

    }

    public static void leastSignificantBitNoise(int noisePeak,FormattedAudioInput input,String filename) {
        Random random = new Random();

        byte[] inputBytes = input.stream.toByteArray();
        int jumps =input.format.getSampleSizeInBits()/Byte.SIZE;


        byte[] outputBytes = new byte[inputBytes.length];
          int messageIndex = 0;
        //todo check how channels are encoded again
        for(int i = 0;i*jumps+1<inputBytes.length;i++) {

            outputBytes[i*jumps] = inputBytes[i*jumps];

            outputBytes[i*jumps+1] = ByteUtil.encodeNthBit(noisePeak,inputBytes[i * jumps+1], random.nextInt(10)<5);



        }




        //ByteArrayInputStream inputStream = new ByteArrayInputStream(outputBytes);
        try {
            AudioIOUtilities.writeAudioFromByteArray(outputBytes, input, filename);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public static void whiteNoiseEncoding(FormattedAudioInput input,String filename) {


        byte[] inputBytes = input.stream.toByteArray();
        int jumps =input.format.getSampleSizeInBits()/Byte.SIZE;
        //todo test if 2
        //System.out.println(jumps);
        byte[] outputBytes = new byte[inputBytes.length];

        //todo check how channels are encoded again
        for(int i = 0;i*jumps+1<inputBytes.length;i++) {

            outputBytes[i*jumps] = inputBytes[i*jumps];

            outputBytes[i*jumps+1] = ByteUtil.addWhiteNoise(inputBytes[i*jumps+1],1,1);



        }



        try {
            AudioIOUtilities.writeAudioFromByteArray(outputBytes, input, filename);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

}
