package com.webapp.util;

public class DeadlockExample {
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(new LockingTask(lock1, lock2, "Thread 1"));
        Thread thread2 = new Thread(new LockingTask(lock2, lock1, "Thread 2"));

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static class LockingTask implements Runnable {
        private final Object firstLock;
        private final Object secondlock;
        private final String name;

        public LockingTask(Object firstLock, Object secondlock, String name) {
            this.firstLock = firstLock;
            this.secondlock = secondlock;
            this.name = name;
        }

        @Override
        public void run() {
            synchronized (firstLock) {
                System.out.println(name + ":Holding " + firstLock);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(name + ":Waiting for " + secondlock);
                synchronized (secondlock) {
                    System.out.println(name + ": " + secondlock + "received");
                }
            }
        }
    }
}