package com.example.tarsosdsptest;

import java.util.Arrays;

public class AudioOut {
    private byte[] byteBuffer;
    private int overlap;
    private int bytesProcessing;

    public AudioOut(byte[] byteBuffer, int overlap, int bytesProcessing) {
        this.byteBuffer = byteBuffer;
        this.overlap = overlap;
        this.bytesProcessing = bytesProcessing;
    }

    public byte[] getByteBuffer() {
        return byteBuffer;
    }

    public void setByteBuffer(byte[] byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    public int getOverlap() {
        return overlap;
    }

    public void setOverlap(int overlap) {
        this.overlap = overlap;
    }

    public int getBytesProcessing() {
        return bytesProcessing;
    }

    public void setBytesProcessing(int bytesProcessing) {
        this.bytesProcessing = bytesProcessing;
    }

    @Override
    public String toString() {
        return "AudioOut{" +
                "byteBuffer=" + Arrays.toString(byteBuffer) +
                ", overlap=" + overlap +
                ", bytesProcessing=" + bytesProcessing +
                '}';
    }
}
