public class SynchronizedCounterImpl implements Counter {
    public int i=0;

    @Override
    public synchronized void increment(){
        this.i += 1;
    }

    @Override
    public synchronized void decrement(){
        this.i -= 1;
    }

    @Override
    public int getCounter() {
        return i;
    }
}
