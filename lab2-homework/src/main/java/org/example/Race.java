package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Race {
    private final AbstractSemaphore semaphore;
	private long counter = 0;

	public Race(AbstractSemaphore semaphore) {
		this.semaphore = semaphore;
    }

	public Thread createIncrementThread(){
		return new Thread(() -> {
			for (int j = 0; j < 1000000; j++)
				increase();
		});
	}

	public Thread createDecrementThread(){
		return new Thread(() -> {
			for (int j = 0; j < 1000000; j++)
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

		System.out.println("Result = " + counter);
    }

	public void increase() {
		semaphore.P();
		counter++;
		semaphore.V();
	}

	public void decrease(){
		semaphore.P();
		counter--;
		semaphore.V();
	}
}
