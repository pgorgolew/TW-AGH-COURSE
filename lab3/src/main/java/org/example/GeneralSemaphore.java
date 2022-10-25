package org.example;

class GeneralSemaphore{
    private int resources;
    private final BinSemaphore accessToValue = new BinSemaphore();
    private final BinSemaphore accessToResource = new BinSemaphore();

    public GeneralSemaphore(int resourcesCount) {
        resources = resourcesCount;
    }

    public void P() {
        this.accessToResource.P();

        this.accessToValue.P();

        this.resources--;
        if (this.resources > 0) {
            this.accessToResource.V();
        }

        this.accessToValue.V();
    }

    public void V() {
        this.accessToValue.P();

        this.resources++;
        this.accessToResource.V();

        this.accessToValue.V();
    }
}
