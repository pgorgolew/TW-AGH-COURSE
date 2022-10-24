package org.example;

class BadBinSemaphore extends AbstractSemaphore {
	@Override
	public synchronized void V() {
		if (value == 1){
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
		if (value == 0){
			try {
				this.wait();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
				System.exit(-1);
			}
		}

		value = 0;
	}
}
