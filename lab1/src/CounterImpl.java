public class CounterImpl implements Counter {
    public int i=0;

    @Override
    public void increment(){
        this.i += 1;
    }

    @Override
    public void decrement(){
        this.i -= 1;
    }

    @Override
    public int getCounter() {
        return i;
    }
}
