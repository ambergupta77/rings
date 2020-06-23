package com.uno.lmax;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventPoller;
import com.lmax.disruptor.RingBuffer;
import org.agrona.concurrent.Agent;

/**
 * In LMAX we can have also have java objects stored in ring.
 * Where as in Agrona it is all about byte buffers
 */
public class LMAXAgent implements Agent {
    private int pollCount = 0;

    private final RingBuffer<MyEvent> ringBuffer = RingBuffer.createSingleProducer(LmaxEventFactory.FACTORY, 1024, new BusySpinWaitStrategy());
    private final EventPoller poller = ringBuffer.newPoller();

    private final EventPoller.Handler handler = new EventPoller.Handler() {
        @Override
        public boolean onEvent(Object o, long l, boolean b) throws Exception {
            System.out.println("Event: " + ((MyEvent)o).toString());
            return false;
        }
    };


    @Override
    public int doWork() throws Exception {
        pollCount = 0;
        //for (int i = 0; i < 100; i++) { // what is the importance of this loop ?
            poller.poll(handler);
        //}
        return pollCount;
    }

    @Override
    public String roleName() {
        return null;
    }

    public RingBuffer<MyEvent> getRingBuffer() {
        return ringBuffer;
    }
}
