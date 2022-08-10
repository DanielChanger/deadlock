package org.deadlock;

import lombok.SneakyThrows;

import java.util.List;

public class DeadlockExample {

    private static final Object lock = new Object();
    private static final Object lock2 = new Object();

    @SneakyThrows
    public static void main(String[] args) {
        List<Thread> asyncTasks = List.of(new Thread(DeadlockExample::task1), new Thread(DeadlockExample::task2));
        asyncTasks.forEach(Thread::start);

        while (true) {
            asyncTasks.forEach(thread -> System.out.printf("Thread %s status is %s%n", thread.getName(), thread.getState()));
            Thread.sleep(2000);
        }
    }

    @SneakyThrows
    static void task1() {
        synchronized (lock) {
            System.out.printf("Thread %s executing task1%n", Thread.currentThread().getName());
            Thread.sleep(10000); // do some job
            task2();
        }
    }

    @SneakyThrows
    static void task2() {
        synchronized (lock2) {
            System.out.printf("Thread %s executing task2%n", Thread.currentThread().getState());
            Thread.sleep(5000); // do some job
            task1();
        }
    }
}
