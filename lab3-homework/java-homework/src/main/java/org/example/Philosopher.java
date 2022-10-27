package org.example;

import java.util.Random;

public class Philosopher extends Thread {
    private final int ITER = 100;
    protected final Stick leftStick;
    protected final Stick rightStick;
    private final int id;
    private int eatingCount = 0;
    protected long waitTime = 0;


    public Philosopher(Stick leftStick, Stick rightStick, int id) {
        this.leftStick = leftStick;
        this.rightStick = rightStick;
        this.id = id;
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
        for (int i = 0; i < ITER; i++) {
            think();
            eat();
        }
    }

    protected void eat() {
        long startWaiting = System.nanoTime();

        if (pickUpSticks()){
            eatingCount++;
            safeSleep(10);
            rightStick.release();
        }
        leftStick.release();

        waitTime += System.nanoTime() - startWaiting;
    }

    private void think(){
        safeSleep(100);
    }

    private void safeSleep(int bound){
        try {
            sleep(new Random().nextInt(1, bound));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean pickUpSticks() {
        leftStick.acquire();
        return rightStick.tryAcquire();
    }

    public String getResults() {
        return "PHILOSOPHER ID " + id +
                " \tEATING COUNT " + eatingCount +
                " \tAVG STICK WAIT " + waitTime / (double) ITER +
                " \tAVG STICK WAIT (eating success) " + waitTime / (double) eatingCount;
    }
}
