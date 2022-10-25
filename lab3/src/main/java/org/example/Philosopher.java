package org.example;

import java.util.Random;

public class Philosopher extends Thread {
    private final Stick leftStick;
    private final Stick rightStick;
    private int eatingCount = 0;
    private boolean blockPossibility = true;
    private Arbiter arbiter;

    public Philosopher(Stick leftStick, Stick rightStick) {
        this.leftStick = leftStick;
        this.rightStick = rightStick;
    }

    public Philosopher(Stick leftStick, Stick rightStick, Arbiter arbiter) {
        this.leftStick = leftStick;
        this.rightStick = rightStick;
        this.blockPossibility = false;
        this.arbiter = arbiter;
    }

    public static void safeJoin(Thread thread) {
        try {
            thread.join();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void run() {
        if (blockPossibility)
            runWithBlockPossibility();
        else
            runWithArbiter();

        System.out.println("THREAD " + Thread.currentThread().getId() + " has eatingCount = " + eatingCount);
    }

    public void runWithBlockPossibility() {
        for (int i=0; i<1000; i++){
            naiveEatingAlgorithm();
        }
    }

    public void runWithArbiter() {
        for (int i=0; i<1000; i++){
            arbiter.waitForSpace();
            naiveEatingAlgorithm();
            arbiter.releaseSpace();
        }
    }

    private void naiveEatingAlgorithm() {
//        try {
//            sleep(new Random().nextInt(1, 100));
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        leftStick.acquire();
        System.out.println("THREAD " + Thread.currentThread().getId() + " acquired left");
        rightStick.acquire();
        System.out.println("THREAD " + Thread.currentThread().getId() + " acquired right");

        eatingCount++;

//        try {
//            sleep(new Random().nextInt(1, 3));
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        rightStick.release();
        leftStick.release();
    }
}
