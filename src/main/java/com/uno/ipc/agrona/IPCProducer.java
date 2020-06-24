package com.uno.ipc.agrona;

import org.agrona.IoUtil;
import org.agrona.concurrent.AtomicBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;

import java.io.*;
import java.nio.MappedByteBuffer;

import static org.agrona.concurrent.ringbuffer.RingBufferDescriptor.TRAILER_LENGTH;

public class IPCProducer {
    private static final int SIZE = 2 * 8 * 1024;
    private static final int  MY_EVENT_MSG_ID = 99;
    private static final int  LENGTH = 32;
    private static final int SIZE_OF_INT = 4;
    private static final int SIZE_OF_LONG = 8;
    private static int  count = 0;

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("IPCProducer ...");
        init();
    }

    private static  void init() throws InterruptedException {
        File file = new File(Conf.FILE_NAME);
        IoUtil.deleteIfExists(file);

        MappedByteBuffer mappedByteBuffer = IoUtil.mapNewFile(file, SIZE + TRAILER_LENGTH);
        UnsafeBuffer unsafeBuffer = new UnsafeBuffer(mappedByteBuffer);
        OneToOneRingBuffer ringBuffer = new OneToOneRingBuffer(unsafeBuffer);
        //pause if producer position is not same a consumer position
        /*while (ringBuffer.producerPosition() != ringBuffer.consumerPosition()) {
            Thread.sleep(1000);
            System.out.println("Sleeping for 1 sec");
        }*/
        Thread thread = new Thread (() -> {
            while (true) {
                int index = ringBuffer.tryClaim(MY_EVENT_MSG_ID, LENGTH);
                AtomicBuffer buffer = ringBuffer.buffer();
                buffer.putInt(index, count);
                buffer.putLong(index + SIZE_OF_INT, count * 100);
                buffer.putStringAscii(index + SIZE_OF_INT + SIZE_OF_LONG, "SweetMsg:" + count);
                System.out.println("Written msg no." + count);
                count++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    ringBuffer.commit(index);
                }
            }
        }) ;
        thread.start();
    }

    private static void fileTest() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("/tmp/agrona/testfile.map"));
        String line = null;
        while ((line = reader.readLine()) != null) {
            System.out.println("line: " + line);
        }
    }
}
