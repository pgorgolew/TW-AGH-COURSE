package org.example;

class BinSemaphore extends AbstractSemaphore {
    @Override
    public synchronized void V() {
	    while (value == 1){
			try {
				this.wait();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}

		value = 1;
	    this.notifyAll();
	}

    @Override
    public synchronized void P() {
	    while (value == 0){
			try {
				this.wait();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
				System.exit(-1);
			}
		}

		value = 0;
		this.notifyAll();
    }
}
