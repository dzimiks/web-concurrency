package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static AtomicInteger sumOfScores = new AtomicInteger();
    public static AtomicInteger studentsFinished = new AtomicInteger();
    public static List<Integer> scores = new ArrayList<>();
    public static List<String> students = new ArrayList<>();
    public static long startTime;

    public static void main(String[] args) {
        // Number of students
        int N = getInput();

        EventStream professorStream = new ProfessorStream();
        EventStream assistantStream = new AssistantStream();

        Tutor professor = new Professor(professorStream);
        Tutor assistant = new Assistant(assistantStream);

        ExecutorService tutorExecutor = Executors.newFixedThreadPool(
                Constants.TUTOR_NUMBER_OF_THREADS);
        tutorExecutor.execute(professor);
        tutorExecutor.execute(assistant);

        ScheduledExecutorService studentExecutor = Executors.newScheduledThreadPool(
                Constants.STUDENT_NUMBER_OF_THREADS);
        // Start
        startTime = System.currentTimeMillis();

        for (int i = 0; i < N; i++) {
            Student student = new Student(professorStream, assistantStream);
            studentExecutor.schedule(student, ThreadLocalRandom.current().nextInt(
                    Constants.STUDENT_RANGE_MIN,
                    Constants.STUDENT_RANGE_MAX + 1),
                    TimeUnit.MILLISECONDS);
        }

        try {
            // Defence duration
            Thread.sleep(Constants.DEFENCE);
            tutorExecutor.shutdownNow();
            studentExecutor.shutdownNow();
            tutorExecutor.awaitTermination(Constants.TERMINATION_TIMEOUT, TimeUnit.SECONDS);
            studentExecutor.awaitTermination(Constants.TERMINATION_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("[Main - InterruptedException]:");
            e.printStackTrace();
        }

        printResult();
    }

    private static int getInput() {
        System.out.print("Enter number of students: ");
        Scanner in = new Scanner(System.in);
        int numberOfStudents = in.nextInt();
        in.close();
        return numberOfStudents;
    }

    private static void printResult() {
        int i = 0;

        System.out.println("\n=====================");
        System.out.println("Sum Of Scores: " + sumOfScores.intValue());
        System.out.println("Students Finished: " + studentsFinished.intValue());
        System.out.format("Average Score: %.2f\n\n", sumOfScores.doubleValue() / studentsFinished.doubleValue());
        System.out.println("All scores: " + scores);
        System.out.println("All students: " + students);
        System.out.println("=====================\n");

        for (String student : students) {
            System.out.format("[%02d]: %s -> %d\n", (i + 1), student, scores.get(i++));
        }
    }
}
