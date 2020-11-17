package com.tradeSecrets;

import javax.sound.sampled.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class AudioIOUtilities {

    public static File createFile(String dir) {
        File file = new File(dir);
        try {
            if (file.createNewFile()) {
                System.out.println("File created : " + file.getName() );
            }else {
                System.out.println("file  "+file.getName() +"\" already exists");
            }
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return file;



    }
    public static FormattedAudioInput readToByteArray(File file) throws UnsupportedAudioFileException,IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);

        int bytesPerFrame = audioInputStream.getFormat().getFrameSize();

        // Set an arbitrary buffer size of 1024 frames.
        int numBytes = 1024 * bytesPerFrame;
        byte[] audioBytes = new byte[numBytes];

        int numBytesRead;

        try {

            while ((numBytesRead = audioInputStream.read(audioBytes)) != -1) {
                outputStream.write(audioBytes, 0, numBytesRead);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return new FormattedAudioInput(audioInputStream.getFormat(), outputStream, audioInputStream.getFrameLength());
    }
    public static void writeAudioFromByteArray(byte[] data, FormattedAudioInput formattedAudio, String name) throws IOException {
        ByteArrayInputStream inputStream =  new ByteArrayInputStream(data);

        File outputFile = createFile(name);
        //InputStream stream, AudioFormat format, long length
        AudioInputStream audioInputStream = new AudioInputStream(inputStream, formattedAudio.format, formattedAudio.frameLength);
        //todo fix hard code
        AudioFileFormat.Type type = AudioFileFormat.Type.WAVE;

        if(outputFile!=null) {
            System.out.println(outputFile.getAbsolutePath());
            writeAudio(audioInputStream,type,outputFile);
        }




    }


    public static int writeAudio(AudioInputStream audioInputStream, AudioFileFormat.Type fileType, File outputFile) throws IllegalArgumentException,IOException {



        if (AudioSystem.isFileTypeSupported(fileType, audioInputStream)) {
            AudioSystem.write(audioInputStream,fileType,outputFile);
        } else {
            throw new IllegalArgumentException("Audio format not support by the write system");
        }

        return 1;
    }

    public static void mse(FormattedAudioInput input,FormattedAudioInput embedded) {


        byte[] inputBytes = input.stream.toByteArray();
        byte[] embeddedBytes = embedded.stream.toByteArray();

        if(input.frameLength!=embedded.frameLength) {
            throw new IllegalArgumentException("NON-EQUAL FRAME LENGTH");
        } else if(input.format.equals(embedded.format)) {
            throw new IllegalArgumentException("NON-EQUAL FORMAT");
        }
        int jumps =input.format.getSampleSizeInBits()/Byte.SIZE;
        //todo test if 2
        //System.out.println(jumps);

        double rms = 0;
        //todo check how channels are encoded again
        for(int i = 0;i*jumps+1<inputBytes.length;i++) {

            long inputValue  = ByteUtil.littleEndian( inputBytes[i*jumps+1],inputBytes[i*jumps]);
            long embeddedValue  = ByteUtil.littleEndian( embeddedBytes[i*jumps+1],embeddedBytes[i*jumps]);

            if(inputValue!=embeddedValue) {

                double v = Math.pow(inputValue - embeddedValue, 2);
                rms += v/(inputBytes.length/2f );
            }
        }

        double max = Math.pow(2,16)-1;



        double result = 20*Math.log(max) - 10*Math.log(rms);

        System.out.println(result + " : result");

    }


}
