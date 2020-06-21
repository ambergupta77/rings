package com.uno.agrona;

import org.agrona.concurrent.AtomicBuffer;
import org.agrona.concurrent.ringbuffer.RingBuffer;

public class MyEventProducer {
    public static final int  MY_EVENT_MSG_ID = 99;
    public static final int SIZE_OF_INT = 4;
    public static final int SIZE_OF_LONG = 8;

    public static final int  LENGTH = 32;
    private int count = 0;

    private RingBuffer ringBuffer;

    public MyEventProducer(RingBuffer ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void start () {
        Thread thread = new Thread(() -> {
            int index = 0;
            while (true) {
               try {
                   index = ringBuffer.tryClaim(MY_EVENT_MSG_ID, LENGTH);
                   AtomicBuffer buffer = ringBuffer.buffer();
                   buffer.putInt(index, count);
                   buffer.putLong(index + SIZE_OF_INT, count * 100);
                   buffer.putStringAscii(index + SIZE_OF_INT + SIZE_OF_LONG, "SweetMsg:" + count );
                   count++;
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               } finally {
                   ringBuffer.commit(index);
               }
           }
        });
        thread.start();
    }

}
