package org.example;

import java.util.Random;

public class Philosopher extends Thread {
    private final Stick leftStick;
    private final Stick rightStick;
    private final int id;
    private int eatingCount = 0;

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
        for (int i = 0; i < 10000; i++) {
            think();
            eat();
        }

        System.out.println("PHILOSOPHER " + id + " has eatingCount = " + eatingCount);
    }

    protected void eat() {
        leftStick.acquire();
        System.out.println("PHILOSOPHER " + id + " left stick acquired");
        if (rightStick.tryAcquire()){
            System.out.println("PHILOSOPHER " + id + " right stick acquired");
            eatingCount++;
            safeSleep(10);
            System.out.println("PHILOSOPHER " + id + " stop eating -> releasing sticks");
            rightStick.release();
        }
        leftStick.release();
    }

    private void think(){
        System.out.println("PHILOSOPHER " + id + " start thinking");
        safeSleep(100);
        System.out.println("PHILOSOPHER " + id + " stop thinking");
    }

    private void safeSleep(int bound){
        try {
            sleep(new Random().nextInt(1, bound));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
