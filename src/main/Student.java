package main;

import java.util.concurrent.ThreadLocalRandom;

public class Student implements Runnable {
    private EventStream professorStream;
    private EventStream assistantStream;
    private int duration;
    private long startTime;
    private String tutorThread;

    public Student(EventStream professorStream, EventStream assistantStream) {
        this.professorStream = professorStream;
        this.assistantStream = assistantStream;
    }

    @Override
    public void run() {
        long arrivalTime = System.currentTimeMillis();
        int score = -1;
        boolean isProfessorFree = ThreadLocalRandom.current().nextBoolean();

        while (score == -1) {
            EventStream stream = this.assistantStream;
            StringBuilder sb = new StringBuilder();

            try {
                if (isProfessorFree) {
                    stream = this.professorStream;
                }

                score = getScore(stream);

                if (score != -1) {
                    sb.append("Thread: ");
                    sb.append(Thread.currentThread().getName());
                    sb.append(" Arrival: ");
                    sb.append(arrivalTime - Main.startTime);
                    sb.append(" Prof: ");
                    sb.append(tutorThread);
                    sb.append(" TTC: ");
                    sb.append(duration);
                    sb.append(":");
                    sb.append(startTime - Main.startTime);
                    sb.append(" Score: ");
                    sb.append(score);

                    System.out.println(sb.toString());
                    sb.setLength(0);

                    // Save score
                    Main.sumOfScores.addAndGet(score);
                    Main.scores.add(score);
                    Main.studentsFinished.incrementAndGet();
                    Main.students.add(Thread.currentThread().getName());
                    break;
                }

                isProfessorFree = !isProfessorFree;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private int getScore(EventStream stream) throws InterruptedException {
        boolean isSuccess = stream.openConnection();
        int score;

        if (!isSuccess) {
            return -1;
        }

        this.startTime = System.currentTimeMillis();
        Event startEvent = new Event(Thread.currentThread().getName(), Constants.EVENT_TYPE_STARTED);

        if (!stream.offerEvent(startEvent)) {
            throw new InterruptedException();
        }

        try {
            this.tutorThread = (String) stream.getResult(startEvent);

            // Student defence
            this.duration = ThreadLocalRandom.current().nextInt(
                    Constants.STUDENT_DEFENCE_DURATION_MIN,
                    Constants.STUDENT_DEFENCE_DURATION_MAX + 1);
            Thread.sleep(duration);

            Event finishedEvent = new Event(Thread.currentThread().getName(), Constants.EVENT_TYPE_FINISHED);
            stream.offerEvent(finishedEvent);
            score = (int) stream.getResult(finishedEvent);
        } catch (InterruptedException e) {
            score = (int) stream.getResult(startEvent);
            Thread.currentThread().interrupt();
//            System.err.println("[Student - InterruptedException]:");
//            e.printStackTrace();
        } finally {
            stream.endConnection();
        }

        return score;
    }
}
