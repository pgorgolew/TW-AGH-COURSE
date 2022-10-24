package org.example;

class Buffer {
    private final int[] buf;
    private final int maxId;
    private int currentPutId = 0;
    private int currentGetId = 0;
    private boolean canGet = false;
    private boolean canPut = true;

    public Buffer(int buffer_size) {
        buf = new int[buffer_size];
        maxId = buffer_size - 1;
    }

    public void put(int i) {
        buf[currentPutId] = i;
        currentPutId = (currentPutId + 1) % maxId;
        canGet = true;

        if (currentPutId == currentGetId){
            canPut = false;
        }
    }

    public int get() {
        int val = buf[currentGetId];
        currentGetId = (currentGetId + 1) % maxId;
        canPut = true;
        if (currentPutId == currentGetId){
            canGet = false;
        }

        return val;
    }

    public boolean isCanGet() {
        return canGet;
    }

    public boolean isCanPut() {
        return canPut;
    }

}