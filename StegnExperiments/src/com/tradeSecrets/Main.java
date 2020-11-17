package com.tradeSecrets;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;


import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.LinkedList;

import static com.tradeSecrets.AudioIOUtilities.*;
import static com.tradeSecrets.SteganographyUtil.leastSignificantBitEncoding;

public class Main {
    public static final String  TYPE = "type_";
    public static final String  WAVE_URL = "C:\\Users\\Edwin\\StegnExperiments\\resources\\file_example_WAV_1MG.wav";
    public static final String  WAVE_URL_10 = "C:\\Users\\Edwin\\StegnExperiments\\resources\\file_example_WAV_10MG.wav";
    public static final String ENCODED_INFO = "C:\\Users\\Edwin\\StegnExperiments\\master.wav";
    public static final String extra ="C:\\Users\\Edwin\\StegnExperiments\\extras.wav";






    public static final String MESSAGE = "Do y'all consent to me using the last few messages as proof increase text length and see what happens \n" +
            "[11:46 AM]\n" +
            "extra you say? ..... I would never do such a thing ";
    public static final String EXTENSION = ".wav";
    public static final String OUTPUTDIR = "\\OUTPUT\\";

    public static void main(String[] args) {
	// write your code here
        try {
           FormattedAudioInput inputStream = readToByteArray(new File(WAVE_URL));
           // FormattedAudioInput extraStream = readToByteArray(new File(extra));
            //MessageText text = new MessageText();
            //AudioIOUtilities.mse(inputStream,extraStream);*/
            boolean[] booleanBitArray = new boolean[32];
            String lengthPrefix = Integer.toBinaryString(16);
            System.out.println(lengthPrefix);
            for(int bitIndex = 0;bitIndex<16;bitIndex++) {

                if(bitIndex<16-lengthPrefix.length()) {
                    booleanBitArray[bitIndex] = false;
                }else {

                    booleanBitArray[bitIndex] = lengthPrefix.charAt(bitIndex-(16-lengthPrefix.length()))=='1';
                }

            }

            for(boolean boo:booleanBitArray) {
                if(boo){
                    System.out.print(1);
                }else {
                    System.out.print(0);
                }
            }
            System.out.println("");

            int test =  6;

            System.out.println(Integer.toBinaryString(6));

            PerformanceMetrics.psnr(PerformanceMetrics.LSB_RAW,PerformanceMetrics.LSB);
            //PerformanceMetrics.psnr(PerformanceMetrics.ECHO_RAW,PerformanceMetrics.ECHO);
            //PerformanceMetrics.psnr(PerformanceMetrics.PhASE_RAW,PerformanceMetrics.PhASE);
            //PerformanceMetrics.psnr(PerformanceMetrics.DSSS_RAW,PerformanceMetrics.DSSS);
            //SteganographyUtil.leastSignificantBitEncoding(inputStream,"extra.wav",text.text);


            EncryptionTool encryptionTool = new EncryptionTool();
            String test_message = encryptionTool.encrypt("smledw002");
            System.out.println("Extracting Embedded signal from : "+  "extras.wav");
            SteganographyUtil.leastSignificantBitPrefixEncoding(inputStream,"extras.wav",test_message);
            FormattedAudioInput extrasStream = readToByteArray(new File(extra));
            System.out.println("Extraction successful");

            ArrayList<Boolean> booleanArrayList = SteganographyUtil.leastSignificantBitDecoding(extrasStream);

            byte[] bytes = ByteUtil.binaryListToByteArray(booleanArrayList);
            String result =  new String(bytes,StandardCharsets.UTF_8);

            int size =ByteUtil.extractHeader(booleanArrayList);
            System.out.println( size);
            //PerformanceMetrics.cipherTextTest(booleanArrayList,encryptionTool);

            String encryptedRetrieved = result.substring(2,ByteUtil.extractHeader(booleanArrayList)+2);


           // System.out.println(ByteUtil.littleEndian(bytes[1],bytes[0])/8);
           // System.out.println("space");

            System.out.println("Header size "+ ByteUtil.extractHeader(booleanArrayList));





            System.out.println("Extracted encrypted test_message : " + encryptedRetrieved);

            String decyptedMessage = encryptionTool.decrypt(encryptedRetrieved);

            System.out.println("Decrypted Extracted message : "+decyptedMessage);


            //FormattedAudioInput inputStream = readToByteArray(new File(WAVE_URL));

            //FormattedAudioInput inputStream = readToByteArray(new File(WAVE_URL));
            //todo un-do encode decode demo


           // ArrayList<Boolean> booleanArrayList = SteganographyUtil.leastSignificantBitDecoding(inputStream);

            //byte[] byteData = ByteUtil.binaryListToByteArray(booleanArrayList);


            //String decode = new String(byteData,StandardCharsets.UTF_8);
           // System.out.println(decode);


            //SteganographyUtil.leastSignificantBitNoise(0,inputStream,TYPE+"_output.wav");
            //SteganographyUtil.whiteNoiseEncoding(inputStream,TYPE+"_White_output.wav");









        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    // todo  Add extension support



    public static void data() {
        byte[] a = {0xA,0xb,0xc,0xd,0xf};

        boolean[] z = ByteUtil.byteArrayToBooleanBitArray(a);
        byte[] bytes = new byte[5];

        bytes[0] = ByteUtil.encodeBit((byte) 0xff,0,false);
        bytes[1] = ByteUtil.encodeBit((byte) 0xff,1,false);
        bytes[2] = ByteUtil.encodeBit((byte) 0xff,2,false);
        bytes[3] = ByteUtil.encodeBit((byte) 0xff,3,false);
        bytes[4] = ByteUtil.encodeBit((byte) 0xff,4,false);

        for(byte k:bytes) {
            System.out.println(Integer.toHexString(k));
        }
        System.out.println("Set true");
        bytes[0] = ByteUtil.encodeBit((byte) 0x60,0,true);
        bytes[1] = ByteUtil.encodeBit((byte) 0x60,1,true);
        bytes[2] = ByteUtil.encodeBit((byte) 0x60,2,true);
        bytes[3] = ByteUtil.encodeBit((byte) 0x60,3,true);
        bytes[4] = ByteUtil.encodeBit((byte) 0x60,4,true);

        for(byte k:bytes) {
            System.out.println(Integer.toHexString(k));
        }

        //encoding test





        System.out.println("");
        //System.out.println(ByteUtil.byteArrayToInt(a));
        byte b[] = {ByteUtil.leastSignificantEncode(a[0],true)};
        //System.out.println(ByteUtil.byteArrayToInt(b));
        FormattedAudioInput outputStream ;
        ByteArrayInputStream inputStream;

        AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;


        try{
            outputStream = readToByteArray(new File(WAVE_URL));

            inputStream =  new ByteArrayInputStream(outputStream.stream.toByteArray());

            File outputFile = createFile("encoded.wav");
            //InputStream stream, AudioFormat format, long length
            AudioInputStream audioInputStream = new AudioInputStream(inputStream,outputStream.format,outputStream.frameLength);


            if(outputFile!=null) {

                writeAudio(audioInputStream,fileType,outputFile);

                leastSignificantBitEncoding(outputStream,"love.wav","Love, guaranteed\n\n");
            }


        }catch (Exception e) {
            e.printStackTrace();
        }

    }




    public static void listOfFiles() throws IOException {
            //Creating a File object for directory
            File directoryPath = new File("C:\\Users\\Edwin\\OneDrive\\2020\\Control");
            //List of all files and directories
            File filesList[] = directoryPath.listFiles();
            System.out.println("List of files and directories in the specified directory:");
            for(File file : filesList) {
                System.out.println("File name: "+file.getName());
                System.out.println("File path: "+file.getAbsolutePath());


                if(isExtension(file.getName(),EXTENSION)) {
                    System.out.println("File correct Extension : "+ file.getName());
                }

            }
    }

    public static void noiseTests(String baseDir,String outPutDir, int iterations) {
        try {
            FormattedAudioInput inputStream = readToByteArray(new File(WAVE_URL));
            LinkedList<File> waveFiles = getWavFiles(baseDir);
            File directory =  new File(baseDir+OUTPUTDIR);
            if(!directory.exists() & !directory.mkdir()) {
                throw new IOException("output dir couldn't be created");
            }

            for(File file:waveFiles) {

                for(int i = 0;i<iterations;i++) {
                    String dir = baseDir+outPutDir+"white_noise"+i+"_"+file.getName();

                    File tmpfile = new File(dir);
                    if(tmpfile.exists()|tmpfile.createNewFile()) {
                        SteganographyUtil.whiteNoiseEncoding(inputStream,dir);
                    }

                }

            }


        }catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static LinkedList<File> getWavFiles(String baseDirectory) {
        File directoryPath = new File(baseDirectory);
        File[]  filesList= directoryPath.listFiles();
        LinkedList<File> files = new LinkedList<>();
        for(File file : filesList) {
            System.out.println("File name: "+file.getName());
            System.out.println("File path: "+file.getAbsolutePath());

            if(isExtension(file.getName(),EXTENSION)) {
                System.out.println("File correct Extension : "+ file.getName());
                files.add(file);
            }
        }

        return files;

    }



    public static boolean isExtension(String fileName, String extension) {
        if(fileName.length()<3) {
            throw new IllegalArgumentException();
        }
        String extracted = fileName.substring(fileName.length()-4,fileName.length());
        return extracted.equalsIgnoreCase(EXTENSION);

    }



    public LinkedList<ByteSample> readAudioFile(File file) throws UnsupportedAudioFileException,IOException {


        //int totalFramesRead = 0;
        LinkedList<ByteSample> samples = new LinkedList<>();

        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);


        //SteganographyUtil.leastSignificantBitEncoding();

        int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
        AudioFormat format = audioInputStream.getFormat();
        // Set an arbitrary buffer size of 1024 frames.
        int numBytes = 1024 * bytesPerFrame;
        int readLoopCount =0;
        byte[] audioBytes = new byte[numBytes];
        try {

            int numBytesRead;

            while ((numBytesRead = audioInputStream.read(audioBytes)) != -1) {


                samples.add(new ByteSample(numBytesRead,audioBytes,readLoopCount,format));
                readLoopCount ++;
            }

        } catch (Exception ex) {
            // Handle the error...
            ex.printStackTrace();
            System.exit(0);

        }

        return samples;

    }



}
