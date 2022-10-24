package org.example;

class GeneralSemaphore extends AbstractSemaphore{
		private int value;
		private final BinSemaphore accessToValue = new BinSemaphore();
		private final BinSemaphore isAbleToDecrease = new BinSemaphore();

		public GeneralSemaphore(int val) {
			value = val;
		}

		public void P() { //acquire
			this.isAbleToDecrease.P();

			this.accessToValue.P();

			this.value--;
			if (this.value > 0) {
				this.isAbleToDecrease.V();
			}

			this.accessToValue.V();
		}

		public void V() { //release
			this.accessToValue.P();

			this.value++;
			if (this.value >= 1) {
				this.isAbleToDecrease.V();
			}

			this.accessToValue.V();
		}
}