package com.udtech.thinice.protocol;

/**
 * Created by JOkolot on 05.02.2016.
 */
public interface Protocol {
    public byte[] getCharge();
    public byte[] setTemperature(int temperature);
    public byte[] on();
    public byte[] off();

}
