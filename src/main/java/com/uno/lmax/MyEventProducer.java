package com.uno.lmax;

import com.lmax.disruptor.RingBuffer;

import java.util.function.Supplier;

public class MyEventProducer {

    private RingBuffer<MyEvent> ringBuffer;
    private Supplier<MyEvent> myEventSupplier = MyEvent::new;

    private int count = 0;
    public MyEventProducer(RingBuffer<MyEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void start() {
        Thread thread = new Thread (() -> {
            while (true) {
                try {
                    long idx = ringBuffer.next();
                    MyEvent myEvent = ringBuffer.get(idx);
                    myEvent.reset();
                    myEvent.setId(count);
                    myEvent.setName("Name is:" +count );
                    count++;
                    ringBuffer.publish(idx);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

}