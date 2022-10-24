package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CountingSemaphoreTest {
    Shared shared;

    public CountingSemaphoreTest(int numOfThreads, int semaphoreVal) {
        shared = new Shared(semaphoreVal);

        List<Thread> threads = Stream.generate(this::createThread)
                .limit(numOfThreads)
                .collect(Collectors.toCollection(ArrayList::new));

        threads.forEach(Thread::start);
        threads.forEach(Race::safeJoin);
    }

    public Thread createThread(){
        return new Thread(() -> shared.randomSleepForThread());
    }
}
