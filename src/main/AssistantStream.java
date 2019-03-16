package main;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class AssistantStream extends EventStream {

    private ReentrantLock lock;

    public AssistantStream() {
        this.events = new SynchronousQueue<>();
        this.lock = new ReentrantLock();
    }

    @Override
    public boolean lock() throws InterruptedException {
        return this.lock.tryLock(Constants.ASSISTANT_LOCK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public void unlock() {
        this.lock.unlock();
    }
}
