package org.example;

import java.util.Random;

class Shared {
    private final GeneralSemaphore semaphore;

    Shared(int k) {
        semaphore = new GeneralSemaphore(k);
    }

    public void randomSleepForThread() {
        semaphore.P();

        System.out.println(
            "AFTER P(). Thread " + Thread.currentThread().getId()
        );

        try {
            Thread.sleep(new Random().nextInt(1, 10));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(
             "JUST BEFORE V(). Thread " + Thread.currentThread().getId()
        );

        semaphore.V();
    }
}