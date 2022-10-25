package org.example;

public class Stick {
    private int leftPhilosopherID;
    private int rightPhilosopherId;

    private BinSemaphore semaphore = new BinSemaphore();

    public Stick(int leftPhilosopherID, int rightPhilosopherId) {
        this.leftPhilosopherID = leftPhilosopherID;
        this.rightPhilosopherId = rightPhilosopherId;
    }

    public void acquire(){
        semaphore.P();
    }

    public void release(){
        semaphore.V();
    }

    public boolean tryAcquire(){
        return semaphore.tryP();
    }

    public String getInfo(){
        return "Philosophers id's: LEFT -> " + leftPhilosopherID + " RIGHT -> " + rightPhilosopherId;
    }

}
