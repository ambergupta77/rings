package com.uno.agrona;

import org.agrona.ErrorHandler;
import org.agrona.concurrent.AgentRunner;
import org.agrona.concurrent.BusySpinIdleStrategy;

public class AgronaApp {
    public static void main(String[] args) {
        System.out.println("Main called...");
        AgronaAgent agronaAgent = new AgronaAgent();
        MyEventProducer myEventProducer  = new MyEventProducer(agronaAgent.getRingBuffer());
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
