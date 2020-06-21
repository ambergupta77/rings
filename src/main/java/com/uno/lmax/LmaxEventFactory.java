package com.uno.lmax;

import com.lmax.disruptor.EventFactory;

public class LmaxEventFactory {
    /*public static final EventFactory<MyEvent> FACTORY = new EventFactory<MyEvent>() {
        @Override
        public MyEvent newInstance() {
            return new MyEvent();
        }
    };*/
    public static final EventFactory<MyEvent> FACTORY = MyEvent::new;
}
