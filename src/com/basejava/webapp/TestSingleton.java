package com.basejava.webapp;

public class TestSingleton {
    private static TestSingleton instance;

    private TestSingleton() {
    }

    public static TestSingleton getInstance() {
        if (instance == null) {
            instance = new TestSingleton();
        }
        return instance;
    }

    public void printTest() {
        System.out.println("Test");
    }

    public static void main(String[] args) {
        TestSingleton.getInstance().printTest();
    }
}
