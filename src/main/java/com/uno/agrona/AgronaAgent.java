package com.uno.agrona;
/**
 * Unlike LMAX here we can only have byte buffers stored.
 */

import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.Agent;
import org.agrona.concurrent.UnsafeBuffer;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;
import org.agrona.concurrent.ringbuffer.RingBuffer;

import java.nio.ByteBuffer;

import static org.agrona.concurrent.ringbuffer.RingBufferDescriptor.TRAILER_LENGTH;

public class AgronaAgent implements Agent {
    public static final int  MY_EVENT_MSG_ID = 99;
    private RingBuffer ringBuffer;

    public AgronaAgent(RingBuffer ringBuffer) {
        this.ringBuffer = ringBuffer;
    }


    @Override
    public int doWork() throws Exception {
        return ringBuffer.read((int msgTypeId ,  MutableDirectBuffer mutableDirectBuffer, int index, int length) -> {
            if (msgTypeId == MY_EVENT_MSG_ID) {
                    int anInt = mutableDirectBuffer.getInt(index);
                    long aLong = mutableDirectBuffer.getLong(index + 4);
                    String stringAscii = mutableDirectBuffer.getStringAscii(index + 4 + 8);
                    System.out.println("Received messages with Int: " + anInt + " Long:" + aLong + " String: " + stringAscii);
            }
        });
        /*return  ringBuffer.read(new MessageHandler() {
            @Override
            public void onMessage(int msgTypeId ,  MutableDirectBuffer mutableDirectBuffer, int index, int length) {
                if (msgTypeId == MY_EVENT_MSG_ID) {
                    int anInt = mutableDirectBuffer.getInt(index);
                    long aLong = mutableDirectBuffer.getLong(index + 4);
                    String stringAscii = mutableDirectBuffer.getStringAscii(index + 4 + 8);
                    System.out.println("Messages has Int: " + anInt + " Long:" + aLong + " String: " + stringAscii);
                }
            }
        });*/
    }
    @Override
    public String roleName() {
        return null;
    }
}
