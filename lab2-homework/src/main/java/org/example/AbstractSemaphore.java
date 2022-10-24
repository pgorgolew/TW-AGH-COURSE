package org.example;

abstract class AbstractSemaphore {
    protected boolean accessToValue = true;
    protected int threadsInQueue = 0;

    abstract public void P();
    abstract public void V();
}