package main;

import java.util.concurrent.BlockingQueue;

public abstract class EventStream {

    public BlockingQueue<Event> events;

    public abstract boolean lock() throws InterruptedException;

    public abstract void unlock();

    public boolean openConnection() throws InterruptedException {
        return lock();
    }

    public void endConnection() {
        unlock();
    }

    public boolean offerEvent(Event event) throws InterruptedException {
        return this.events.offer(event,
                Constants.EVENT_STREAM_TIMEOUT,
                java.util.concurrent.TimeUnit.SECONDS);
    }

    public Event takeEvent() throws InterruptedException {
        return this.events.take();
    }

    public Object getResult(Event event) throws InterruptedException {
        return event.getResult();
    }
}
