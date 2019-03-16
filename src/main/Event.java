package main;

import java.util.concurrent.CountDownLatch;

public class Event {

    private final String eventSender;
    private final String eventType;
    private final CountDownLatch latch;
    private Object result;

    public Event(String eventSender, String eventType) {
        this.eventSender = eventSender;
        this.eventType = eventType;
        this.latch = new CountDownLatch(1);
    }

    public Object getResult() throws InterruptedException {
        this.latch.await();
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
        this.latch.countDown();
    }

    public String getEventSender() {
        return eventSender;
    }

    public String getEventType() {
        return eventType;
    }
}
