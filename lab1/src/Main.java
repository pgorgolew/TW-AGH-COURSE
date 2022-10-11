public class Main {

    private static void testCounter(Counter counter){
        Thread incrementThread = new Thread(() -> {
            for (int i=0; i<1000000; i++){
                counter.increment();
            }
        });

        Thread decrementThread = new Thread(() -> {
            for (int i=0; i<1000000; i++){
                counter.decrement();
            }
        });

        incrementThread.start();
        decrementThread.start();

        try {
            incrementThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            decrementThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(counter.getCounter());

    }

    private static void testMaxThreads(){
        int i=0;
        while(true){
            System.out.println(i);
            new Thread () {
                public void run () {
                    for (;;);
                }
            }.start();
            i+=1;
        }
    }

    public static void main(String[] args) {
        testCounter(new SynchronizedCounterImpl()); // always 0
        testCounter(new CounterImpl()); //different things
        // testMaxThreads(); 3083
    }
}