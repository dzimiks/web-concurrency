package main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Tutor implements Runnable {

    public EventStream eventStream;
    private List<Event> allEvents = new ArrayList<>();

    @Override
    public void run() {
        while (true) {
            try {
                Event event = eventStream.takeEvent();

                if (event.getEventType().equals(Constants.EVENT_TYPE_STARTED)) {
                    allEvents.add(event);
                    event.setResult(Thread.currentThread().getName());
                } else if (event.getEventType().equals(Constants.EVENT_TYPE_FINISHED)) {
                    event.setResult(ThreadLocalRandom.current().nextInt(
                            Constants.GRADE_MIN,
                            Constants.GRADE_MAX + 1));
//                    event.setResult(10);

                    // Remove old events
                    List<Event> foundedEvents = new ArrayList<>();

                    for (Event e : allEvents) {
                        if (e.getEventSender().equals(event.getEventSender())) {
                            foundedEvents.add(e);
                        }
                    }

                    allEvents.removeAll(foundedEvents);
                }
            } catch (InterruptedException e) {
                // TODO
                for (Event startEvent : allEvents) {
//                    startEvent.setResult(10);
                    startEvent.setResult(ThreadLocalRandom.current().nextInt(
                            Constants.GRADE_MIN,
                            Constants.GRADE_MAX + 1));
                }

                Thread.currentThread().interrupt();
//                System.err.println("[Tutor - InterruptedException]:");
//                e.printStackTrace();
                break;
            }
        }
    }
}
