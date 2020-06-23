package com.uno.ipc.agrona;

import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.Agent;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;

public class IPCConsumerAgent implements Agent {

    private OneToOneRingBuffer ringBuffer;
    public static final int  MY_EVENT_MSG_ID = 99;

    public IPCConsumerAgent(OneToOneRingBuffer ringBuffer) {
            this.ringBuffer = ringBuffer;
    }

    @Override
    public int doWork() throws Exception {
        return ringBuffer.read((int msgTypeId ,  MutableDirectBuffer mutableDirectBuffer, int index, int length) -> {
            if (msgTypeId == MY_EVENT_MSG_ID) {
                int anInt = mutableDirectBuffer.getInt(index);
                long aLong = mutableDirectBuffer.getLong(index + 4);
                String stringAscii = mutableDirectBuffer.getStringAscii(index + 4 + 8);
                System.out.println("Messages has Int: " + anInt + " Long:" + aLong + " String: " + stringAscii);
            }
        });
    }

    @Override
    public String roleName() {
        return null;
    }
}
