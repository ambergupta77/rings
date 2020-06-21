package com.uno.lmax;

import org.agrona.ErrorHandler;
import org.agrona.concurrent.Agent;
import org.agrona.concurrent.AgentRunner;
import org.agrona.concurrent.BusySpinIdleStrategy;

public class LMAXApp {
    public static void main(String[] args) {
        System.out.println("main");
        LMAXAgent agent = new LMAXAgent();
        MyEventProducer myEventProducer = new MyEventProducer(agent.getRingBuffer());
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
