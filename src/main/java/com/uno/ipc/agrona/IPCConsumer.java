package com.uno.ipc.agrona;

import org.agrona.ErrorHandler;
import org.agrona.IoUtil;
import org.agrona.concurrent.AgentRunner;
import org.agrona.concurrent.BusySpinIdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;

import java.io.File;
import java.nio.MappedByteBuffer;

import static org.agrona.concurrent.ringbuffer.RingBufferDescriptor.TRAILER_LENGTH;

public class IPCConsumer {
    public static final int SIZE = 2 * 8 * 1024;

    public static void main(String[] args) {
        System.out.println("Consumer");
        File file = new File("/tmp/agrona/shared-file.map");

        MappedByteBuffer mappedByteBuffer = IoUtil.mapExistingFile(file, "Ring buffer file");
        UnsafeBuffer unsafeBuffer = new UnsafeBuffer(mappedByteBuffer);
        OneToOneRingBuffer ringBuffer = new OneToOneRingBuffer(unsafeBuffer);
        IPCConsumerAgent ipcConsumerAgent = new IPCConsumerAgent(ringBuffer);

        AgentRunner agentRunner = new AgentRunner(new BusySpinIdleStrategy(), new ErrorHandler() {
            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        }, null, ipcConsumerAgent);
        agentRunner.run();
    }
}
