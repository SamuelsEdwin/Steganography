package com.tradeSecrets;

import javax.sound.sampled.AudioFormat;

public class ByteSample {
    int numberOfBytes;
    byte[] audioData;
    int index;
    AudioFormat format;
    public ByteSample(int numberOfBytes, byte[] audioData, int index, AudioFormat format) {
        this.index = index;
        this.format = format;
        this.numberOfBytes = numberOfBytes;
        this.audioData = new byte[numberOfBytes];
        // ensureCapacity(count + len);
        //System.arraycopy(b, off, buf, count, len)
        this.audioData = audioData;
    }

}
