package com.basejava.webapp;

import java.util.ArrayList;
import java.util.List;

public class MainDeadlock {
    private static int counter;
    private static final Object LOCK1 = new Object();
    private static final Object LOCK2 = new Object();

    public static final int COUNT_OPERATIONS = 100000;
    public static final int COUNT_THREAD = 2;

    public static void main(String[] args) {
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < COUNT_THREAD; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < COUNT_OPERATIONS; j++) {
                    inc();
                }
            });
            thread.start();
            threadList.add(thread);
        }

        for (int i = 0; i < COUNT_THREAD; i++) {
            try {
                threadList.get(i).join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Counter: " + counter);
    }

    private static void inc() {
        synchronized (counter % 2 == 0 ? LOCK1 : LOCK2) {
            synchronized (counter % 2 == 0 ? LOCK2 : LOCK1) {
                counter++;
            }
        }
    }
}
