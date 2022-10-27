package org.example;

import java.util.concurrent.Semaphore;

public class Stick {
    private final Semaphore semaphore = new Semaphore(1);

    public void acquire(){
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void release(){
        semaphore.release();
    }

    public boolean tryAcquire(){
        return semaphore.tryAcquire();
    }

}
