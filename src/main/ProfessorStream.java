package main;

import java.util.concurrent.*;

public class ProfessorStream extends EventStream {

    private Semaphore semaphore;
    private CyclicBarrier barrier;

    public ProfessorStream() {
        this.events = new LinkedBlockingQueue<>();
        this.semaphore = new Semaphore(Constants.PROFESSOR_SEMAPHORE_PERMITS);
        this.barrier = new CyclicBarrier(Constants.PROFESSOR_BARRIER_PARTIES);
    }

    @Override
    public boolean lock() throws InterruptedException {
        try {
            boolean isAcquired = semaphore.tryAcquire(
                    Constants.PROFESSOR_LOCK_TIMEOUT,
                    TimeUnit.MILLISECONDS);

            if (!isAcquired) {
                return false;
            }

            this.barrier.await(Constants.PROFESSOR_LOCK_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (BrokenBarrierException e) {
//            System.err.println("[ProfessorStream - BrokenBarrierException]:");
//            e.printStackTrace();
            return false;
        } catch (TimeoutException e) {
            this.semaphore.release();
//            System.err.println("[ProfessorStream - TimeoutException]:");
//            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public void unlock() {
        this.semaphore.release();
    }
}
