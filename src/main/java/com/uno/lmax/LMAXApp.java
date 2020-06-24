package com.uno.lmax;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import org.agrona.ErrorHandler;
import org.agrona.concurrent.Agent;
import org.agrona.concurrent.AgentRunner;
import org.agrona.concurrent.BusySpinIdleStrategy;

public class LMAXApp {
    private static final RingBuffer<MyEvent> ringBuffer = RingBuffer.createSingleProducer(LmaxEventFactory.FACTORY, 1024, new BusySpinWaitStrategy());

    public static void main(String[] args) {
        System.out.println("main");
        LMAXAgent agent = new LMAXAgent(ringBuffer);
        MyEventProducer myEventProducer = new MyEventProducer(ringBuffer);
        myEventProducer.start();
        runAgent(agent);

    }

    static void runAgent (Agent agent) {
        AgentRunner agentRunner = new AgentRunner(new BusySpinIdleStrategy(), new ErrorHandler() {
            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        }, null , agent);
        agentRunner.run();
    }
}
