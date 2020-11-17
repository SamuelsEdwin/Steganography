package com.tradeSecrets;

import javax.sound.sampled.AudioFormat;

import java.io.ByteArrayOutputStream;

public class FormattedAudioInput {
    public AudioFormat format;
    public ByteArrayOutputStream stream;
    public long frameLength;

    public FormattedAudioInput(AudioFormat format, ByteArrayOutputStream stream, Long frameLength) {
        this.format = format;
        this.stream = stream;
        this.frameLength = frameLength;
    }
    /**
     * Creates a newly allocated ByteArrayOutputStream . Its size is the current
     * size of the output stream
     * @return  a ByteArrayOutputStream with a clone of this stream.
     */
    public ByteArrayOutputStream getStream() {

      ByteArrayOutputStream array =  new ByteArrayOutputStream(stream.size());
      array.write(stream.toByteArray(),0,stream.size());
      return array;

    }


}
