package com.basejava.webapp;

public class MainDeadlock {
    private static int counter;
    private static final Object LOCK1 = new Object();
    private static final Object LOCK2 = new Object();

    public static final int COUNT = 100000;

    public static void main(String[] args) {
        Thread thread0 = new Thread(() -> {
            for (int i = 0; i < COUNT; i++) {
                synchronized (LOCK1) {
                    counter++;
                    synchronized (LOCK2) {
                        counter--;
                    }
                }
            }
        });

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < COUNT; i++) {
                synchronized (LOCK2) {
                    counter--;
                    synchronized (LOCK1) {
                        counter++;
                    }
                }
            }
        });

        thread0.start();
        thread1.start();

        try {
            thread0.join();
            thread1.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Counter: " + counter);
    }
}
