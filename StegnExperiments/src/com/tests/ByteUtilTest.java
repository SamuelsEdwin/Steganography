package com.tests;

import com.tradeSecrets.ByteUtil;

import static org.junit.jupiter.api.Assertions.*;

class ByteUtilTest {

    @org.junit.jupiter.api.Test
    void leastSignificantEncode() {
    }

    @org.junit.jupiter.api.Test
    void leastSignificantBitValue() {
    }

    @org.junit.jupiter.api.Test
    void byteArrayToInt() {
    }

    @org.junit.jupiter.api.Test
    void byteArrayToBooleanBitArray() {
    }

    @org.junit.jupiter.api.Test
    void getBit() {
    }

    @org.junit.jupiter.api.Test
    void encodeBit() {
        /*
        bin     Hex
        0	    0
        1	    1
        10	    2
        11	    3
        100	    4
        101	    5
        110	    6
        111	    7
        1000	8
        1001	9
        1010	A
        1011	B
        1100	C
        1101	D
        1110	E
        1111	F
         */
        assertEquals((byte)0xFE,ByteUtil.encodeBit((byte) 0xff,0,false));
        assertEquals((byte)0xFD,ByteUtil.encodeBit((byte) 0xff,1,false));
        assertEquals((byte)0xFB,ByteUtil.encodeBit((byte) 0xff,2,false));
        assertEquals((byte)0xF7,ByteUtil.encodeBit((byte) 0xff,3,false));

        assertEquals((byte)0xEF,ByteUtil.encodeBit((byte) 0xff,4,false));
        assertEquals((byte)0xDF,ByteUtil.encodeBit((byte) 0xff,5,false));
        assertEquals((byte)0xBF,ByteUtil.encodeBit((byte) 0xff,6,false));
        assertEquals((byte)0x7F,ByteUtil.encodeBit((byte) 0xff,7,false));

        assertEquals((byte)0x1,ByteUtil.encodeBit((byte) 0x00,0,true));
        assertEquals((byte)0x2,ByteUtil.encodeBit((byte) 0x00,1,true));
        assertEquals((byte)0x4,ByteUtil.encodeBit((byte) 0x00,2,true));
        assertEquals((byte)0x8,ByteUtil.encodeBit((byte) 0x00,3,true));

        assertEquals((byte)0x10,ByteUtil.encodeBit((byte) 0x00,4,true));
        assertEquals((byte)0x20,ByteUtil.encodeBit((byte) 0x00,5,true));
        assertEquals((byte)0x40,ByteUtil.encodeBit((byte) 0x00,6,true));
        assertEquals((byte)0x80,ByteUtil.encodeBit((byte) 0x00,7,true));




    }
}