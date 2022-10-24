package org.example;

class Consumer extends Thread {
    private final Buffer _buf;

    public Consumer(Buffer _buf) {
        this._buf = _buf;
    }

    public void run() {
        for (int i = 0; i < 100; ++i) {
            while (_buf.isCanPut()){
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(_buf.get());
        }
    }
}