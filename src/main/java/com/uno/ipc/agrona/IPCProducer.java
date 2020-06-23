package com.uno.ipc.agrona;

import org.agrona.IoUtil;
import org.agrona.concurrent.AtomicBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;

import java.io.*;
import java.nio.MappedByteBuffer;

import static org.agrona.concurrent.ringbuffer.RingBufferDescriptor.TRAILER_LENGTH;

public class IPCProducer {
    public static final int SIZE = 2 * 8 * 1024;
    public static final int  MY_EVENT_MSG_ID = 99;
    public static final int  LENGTH = 32;
    public static final int SIZE_OF_INT = 4;
    public static final int SIZE_OF_LONG = 8;
    private int  count = 0;
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("main called...");
        //fileTest();
        IPCProducer producer = new IPCProducer();
        producer.init();

    }

    private void init() throws InterruptedException {
        File file = new File("/tmp/agrona/shared-file.map");
        IoUtil.deleteIfExists(file);
        /**
         * private final ByteBuffer byteBuffer = ByteBuffer.allocateDirect((16 * 1024) + TRAILER_LENGTH);
         *     private final UnsafeBuffer unsafeBuffer = new UnsafeBuffer(byteBuffer);
         *     private final RingBuffer ringBuffer = new OneToOneRingBuffer(unsafeBuffer);
         */

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
                count++;
                System.out.println("Written something");
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
