package main;

public class Constants {

    public static final int STUDENT_RANGE_MIN = 1;
    public static final int STUDENT_RANGE_MAX = 1000;
    public static final int STUDENT_DEFENCE_DURATION_MIN = 500;
    public static final int STUDENT_DEFENCE_DURATION_MAX = 1000;
    public static final int STUDENT_NUMBER_OF_THREADS = 30;

    public static final int TUTOR_NUMBER_OF_THREADS = 2;
    public static final int ASSISTANT_LOCK_TIMEOUT = 700;
    public static final int PROFESSOR_LOCK_TIMEOUT = 700;
    public static final int PROFESSOR_SEMAPHORE_PERMITS = 2;
    public static final int PROFESSOR_BARRIER_PARTIES = 2;

    public static final int DEFENCE = 5000;
    public static final int TERMINATION_TIMEOUT = 2;
    public static final int GRADE_MIN = 0;
    public static final int GRADE_MAX = 10;

    public static final int EVENT_STREAM_TIMEOUT = 2;
    public static final String EVENT_TYPE_STARTED = "STARTED";
    public static final String EVENT_TYPE_FINISHED = "FINISHED";
}
