package com.udtech.thinice.protocol;

/**
 * Created by JOkolot on 05.02.2016.
 */
public class ProtocolV1 implements Protocol {
    @Override
    public byte[] getCharge() {
        return new byte[0];
    }

    @Override
    public byte[] setTemperature(int temperature) {
        return new byte[0];
    }

    @Override
    public byte[] on() {
        return new byte[0];
    }

    @Override
    public byte[] off() {
        return new byte[0];
    }
    private byte[] utfToAnsiToByteArr(String string ){
        
    }
}
