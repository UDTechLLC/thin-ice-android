package com.udtech.thinice.protocol;

import java.io.UnsupportedEncodingException;

/**
 * Created by JOkolot on 05.02.2016.
 */
public interface Protocol {
    public byte[] getCharge();
    public byte[] setTemperature(int temperature) throws UnsupportedEncodingException;
    public byte[] on() throws UnsupportedEncodingException;
    public byte[] off() throws UnsupportedEncodingException;
    public byte[] setRawTemperature(int temperature);
}
