package org.example;

class BinSemaphore {
	private boolean accessToValue = true;
	private int threadsInQueue = 0;

    public synchronized void V() {
		if (this.threadsInQueue > 0) {
			this.notify();
		}
		this.accessToValue = true;
	}

    public synchronized void P() {
		this.threadsInQueue++;
		while (!this.accessToValue) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.threadsInQueue--;
		this.accessToValue = false;
    }

	public synchronized boolean tryP() {
		this.threadsInQueue++;

		if (!this.accessToValue)
			return false;

		this.threadsInQueue--;
		this.accessToValue = false;
		return true;
	}
}
