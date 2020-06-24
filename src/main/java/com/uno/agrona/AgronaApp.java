package com.uno.agrona;

import org.agrona.ErrorHandler;
import org.agrona.concurrent.AgentRunner;
import org.agrona.concurrent.BusySpinIdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;
import org.agrona.concurrent.ringbuffer.RingBuffer;

import java.nio.ByteBuffer;

import static org.agrona.concurrent.ringbuffer.RingBufferDescriptor.TRAILER_LENGTH;

public class AgronaApp {
    private static final ByteBuffer byteBuffer = ByteBuffer.allocateDirect((16 * 1024) + TRAILER_LENGTH);
    private static final UnsafeBuffer unsafeBuffer = new UnsafeBuffer(byteBuffer);
    private static final RingBuffer ringBuffer = new OneToOneRingBuffer(unsafeBuffer);

    public static void main(String[] args) {
        System.out.println("AgronaApp...");

        AgronaAgent agronaAgent = new AgronaAgent(ringBuffer);
        MyEventProducer myEventProducer  = new MyEventProducer(ringBuffer);
        myEventProducer.start();

        AgentRunner agentRunner = new AgentRunner(new BusySpinIdleStrategy(), new ErrorHandler() {
            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        }, null, agronaAgent);
        agentRunner.run();
    }
}
