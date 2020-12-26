package com.example.nfccardemulator;

public interface Emulator {
    public void deactivate();
    public void destroy();
    public byte[] process(byte[] commandAPDU);

}
