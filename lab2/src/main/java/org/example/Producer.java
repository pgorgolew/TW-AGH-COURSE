package org.example;

class Producer extends Thread {
    private final Buffer _buf;

    public Producer(Buffer _buf) {
        this._buf = _buf;
    }

    public void run() {
        for (int i = 0; i < 100; ++i) {
            if (_buf.isCanPut()){
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            _buf.put(i);
        }
    }
}