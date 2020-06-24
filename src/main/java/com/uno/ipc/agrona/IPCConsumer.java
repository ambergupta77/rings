package com.uno.ipc.agrona;

import org.agrona.ErrorHandler;
import org.agrona.IoUtil;
import org.agrona.concurrent.AgentRunner;
import org.agrona.concurrent.BusySpinIdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;

import java.io.File;
import java.nio.MappedByteBuffer;

public class IPCConsumer {
    public static void main(String[] args) {
        System.out.println("IPCConsumer");
        File file = new File(Conf.FILE_NAME);

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
