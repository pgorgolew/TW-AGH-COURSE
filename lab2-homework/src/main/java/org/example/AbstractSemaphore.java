package org.example;

abstract class AbstractSemaphore {
    protected int value = 1;

    abstract public void P();
    abstract public void V();
}