package com.udtech.thinice.protocol;

import com.udtech.thinice.model.Settings;
import com.udtech.thinice.pedometer.Utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Created by JOkolot on 05.02.2016.
 */
public class ProtocolV1 implements Protocol {
    private static final String COMMAND_ON = "*E03";
    private static final String COMMAND_OFF = "*D03";
    private static final String COMMAND_SET_TEMP = "*P01";
    private static final String COMMAND_GET_TEMP = "*R01";
    private static final String COMMAND_GET_CHARGE = "";

    @Override
    public byte[] getCharge() {
        return new byte[0];
    }

    @Override
    public byte[] setTemperature(int temperature) throws UnsupportedEncodingException {
        byte[] bytes = ByteBuffer.allocate(4).putInt(Settings.convertTemperature(temperature) *10).array();
        byte[] value = new byte[2];
        value[0] = bytes[2];
        value[1] = bytes[3];
        return (COMMAND_SET_TEMP+"2" + "00"+bytesToHex(value)).getBytes();
    }

    @Override
    public byte[] on() throws UnsupportedEncodingException {
        return (COMMAND_ON + new String(new byte[]{0x0d})).getBytes("UTF-8");
    }

    @Override
    public byte[] off() throws UnsupportedEncodingException {
        return (COMMAND_OFF + new String(new byte[]{0x0d})).getBytes("UTF-8");
    }

    public static String bytesToHex(byte[] bytes) {
        String result = new String();
        for (int i = 0; i < bytes.length; i++)
            result += Integer.toHexString(bytes[i]);
        return result;
    }
}
