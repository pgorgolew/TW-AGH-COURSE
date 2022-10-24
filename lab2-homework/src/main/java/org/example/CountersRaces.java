package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CountersRaces {
    List<Integer> counters;
    List<Boolean> ableToChange;
    GeneralSemaphore semaphore;

    public CountersRaces(int numOfRaces) {
        counters = Stream.generate(() -> 0).limit(numOfRaces).collect(Collectors.toCollection(ArrayList::new));
        ableToChange = Stream.generate(() -> true).limit(numOfRaces).collect(Collectors.toCollection(ArrayList::new));
        semaphore = new GeneralSemaphore(numOfRaces);
    }

    public Thread createIncrementThread(){
        return new Thread(() -> {
            for (int j = 0; j < 100000; j++)
                increase();
        });
    }

    public Thread createDecrementThread(){
        return new Thread(() -> {
            for (int j = 0; j < 100000; j++)
                decrease();
        });
    }

    private void safeJoin(Thread thread) {
        try {
            thread.join();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void run(int incrementThreadsNum, int decrementThreadsNum) {
        List<Thread> incrementThreads = Stream.generate(this::createIncrementThread)
                .limit(incrementThreadsNum)
                .collect(Collectors.toCollection(ArrayList::new));

        List<Thread> decrementThreads = Stream.generate(this::createDecrementThread)
                .limit(decrementThreadsNum)
                .collect(Collectors.toCollection(ArrayList::new));

        Stream.of(incrementThreads, decrementThreads).forEach(list -> list.forEach(Thread::start));
        Stream.of(incrementThreads, decrementThreads).forEach(list -> list.forEach(this::safeJoin));

        System.out.println("Final List: " + counters);
        System.out.println("Result = " + counters.stream().reduce(Integer::sum));
    }

    private int findIdToChangeValue(){
        for (int i=0; i<counters.size(); i++){
            if (ableToChange.get(i)){
                ableToChange.set(i, false);
                return i;
            }
        }
        return -1;
    }
    public void increase() {
        semaphore.P();
        int id = findIdToChangeValue();
        counters.set(id, counters.get(id) + 1);
        ableToChange.set(id, true);
        System.out.println("increase -> " + counters);
        semaphore.V();
    }

    public void decrease(){
        semaphore.P();
        int id = findIdToChangeValue();
        counters.set(id, counters.get(id) - 1);
        ableToChange.set(id, true);
        System.out.println("decrease -> " + counters);
        semaphore.V();
    }
}
