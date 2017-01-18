package com.udtech.thinice.eventbus.model.bluetooth;

/**
 * Created by JOkolot on 28.04.2016.
 */
public class SendMessage {
    private byte[] command;

    public SendMessage(byte[] command) {
        this.command = command;
    }

    public byte[] getCommand() {
        return command;
    }
}
