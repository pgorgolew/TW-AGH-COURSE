package org.example;

public class ObservedPhilosopher extends Philosopher {
    private final Arbiter arbiter;

    public ObservedPhilosopher(Stick leftStick, Stick rightStick, int id, Arbiter arbiter) {
        super(leftStick, rightStick, id);
        this.arbiter = arbiter;
    }

    @Override
    protected void eat(){
        arbiter.waitInQueue();
        super.eat();
        arbiter.letSomebodyEat();
    }

    @Override
    protected boolean pickUpSticks() {
        leftStick.acquire();
        rightStick.acquire();
        return true;
    }
}
