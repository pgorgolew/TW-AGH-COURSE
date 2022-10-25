package org.example;

import java.util.concurrent.Semaphore;

public class Arbiter {
    Semaphore semaphore;

    public Arbiter(int n){
        semaphore = new Semaphore(n-1);
    }

    public void waitForSpace(){
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void releaseSpace(){
        semaphore.release();
    }
}
