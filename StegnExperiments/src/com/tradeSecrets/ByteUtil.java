package com.tradeSecrets;

import java.util.ArrayList;
import java.util.Random;

public class ByteUtil {

    public static byte leastSignificantEncode(byte data,boolean require ) {

        if(leastSignificantBitValue(data) != require) {
            return   (new Integer(data^1)).byteValue();
        }

        return data;
    }

    public static byte encodeNthBit(int n,byte data,boolean required) {
        if(required) {
            return  (byte) (data | (1 << (n - 1)));
        } else {
            return  (byte) (data | (~(1 << (n-1))));
        }

    }
    public static boolean leastSignificantBitValue(byte data) {
        return  (data & 1 )==1;
    }

    public static int byteArrayToInt(byte[] array)  {


        int value = 0;
        for (int i = 0; i < Integer.BYTES && i< array.length; i++) {
            int shift = i * 8;

            value += (array[array.length-1-i] & 0x000000FF) << shift;

        }
        return value;

    }

    public static boolean[] byteArrayToBooleanBitArray(byte[] byteArray) {
        boolean[] booleanBitArray = new boolean[byteArray.length*Byte.SIZE];
        System.out.println(byteArray.length +"length");
        for(int byteIndex = 0; byteIndex < byteArray.length;byteIndex++) {
            for(int bitIndex=0;bitIndex<Byte.SIZE;bitIndex++) {
                booleanBitArray[byteIndex*Byte.SIZE+bitIndex] = getBit(bitIndex,byteArray[byteIndex]);
            }
        }
        return booleanBitArray;
    }
    public static boolean[] byteArrayToPrefixedBooleanBitArray(byte[] byteArray) {
        boolean[] booleanBitArray = new boolean[byteArray.length*Byte.SIZE +16];
        String lengthPrefix = Integer.toBinaryString(byteArray.length);
        System.out.println("array length : "+byteArray.length);
        System.out.println(Integer.toBinaryString(byteArray.length));

        for(int bitIndex = 0;bitIndex<16;bitIndex++) {

            if(bitIndex<16-lengthPrefix.length()) {
                booleanBitArray[bitIndex] = false;
                System.out.print("0");
            }else {

                booleanBitArray[bitIndex] = lengthPrefix.charAt(bitIndex-(16-lengthPrefix.length()))=='1';
                System.out.print(booleanBitArray[bitIndex] ?"1":"0");
            }

        }
        System.out.println(" : end line");
        for(int byteIndex = 0; byteIndex < byteArray.length;byteIndex++) {
            for(int bitIndex=0;bitIndex<Byte.SIZE;bitIndex++) {
                booleanBitArray[(byteIndex+2)*Byte.SIZE+bitIndex] = getBit(bitIndex,byteArray[byteIndex]);
            }
        }


        return booleanBitArray;
    }
    public static boolean getBit(int index,byte data)
    {

        return ((data >> index) & 0x1)==1;
    }

    public static byte[] binaryListToByteArray(ArrayList<Boolean> binaryData) {
        byte[] byteData = new byte[binaryData.size()/8];
        int i = 0;

        while (i<byteData.length*8) {

            if(i%8==0) {
                byteData[i/8] = 0x0;
            }
            byteData[i/8] = ByteUtil.encodeBit(byteData[i/8],i%8,binaryData.get(i));
            i++;

        }
        return byteData;
    }

    public static byte encodeBit(byte data, int index,boolean value) {
        int mask = 0x01;
        mask = (mask<<index);
        //System.out.println(Integer.toHexString((byte)mask));
        if(value) {
            Integer integer = new Integer(data^mask);
            return integer.byteValue();
        }else {
            mask = ~mask;
            //System.out.println(Integer.toHexString((byte)mask));
            Integer integer = new Integer(data&mask);
            return integer.byteValue();
        }

    }

    public static byte addWhiteNoise(byte input,int stdDeviation,int mean) {

        Random rand = new Random();

        int noise = (byte)((int)rand.nextGaussian()*stdDeviation+mean);

        noise+=input;

        return (byte) noise;




    }

    public static long littleEndian(byte lsBits, byte msBits) {
        long a;

        a = ((msBits&255)<<8);

        a = a|(lsBits&255);

        return a;
    }

    public static int extractHeader(ArrayList<Boolean> booleanArrayList) {
        double result = 0;
        for(int i=0;i<16;i++) {

            if(booleanArrayList.get(i)) {

                result += Math.pow(2,15-i);
            }

        }

        return (int)result;

    }


}
