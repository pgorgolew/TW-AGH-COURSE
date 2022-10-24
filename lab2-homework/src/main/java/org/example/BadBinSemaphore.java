package org.example;

class BadBinSemaphore extends AbstractSemaphore {
	@Override
	public synchronized void V() {
		if (this.threadsInQueue > 0) {
			this.notify();
		}
		this.accessToValue = true;
	}

	@Override
	public synchronized void P() {
		this.threadsInQueue++;
		if (!this.accessToValue) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.threadsInQueue--;
		this.accessToValue = false;
	}
}
