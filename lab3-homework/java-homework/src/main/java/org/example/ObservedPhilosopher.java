package org.example;

public class ObservedPhilosopher extends Philosopher {
    private Arbiter arbiter;

    public ObservedPhilosopher(Stick leftStick, Stick rightStick, int id, Arbiter arbiter) {
        super(leftStick, rightStick, id);
        this.arbiter = arbiter;
    }

    @Override
    protected void eat(){
        arbiter.waitInQueue();
        super.eat();
        arbiter.waitInQueue();
    }
}
