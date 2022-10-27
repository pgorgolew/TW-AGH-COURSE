package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    public static void runStarvingPhilosophers(int n){
        List<Stick> sticks = Stream.generate(Stick::new).limit(n).collect(Collectors.toCollection(ArrayList::new));
        List<Philosopher> philosophers = new ArrayList<>();
        IntStream.range(0, n).forEach(i -> philosophers.add(new Philosopher(sticks.get(i), sticks.get((i+1) % n), i)));
        run(philosophers);
    }

    public static void runPhilosophersWithArbiter(int n){
        Arbiter arbiter = new Arbiter(n);
        List<Stick> sticks = Stream.generate(Stick::new).limit(n).collect(Collectors.toCollection(ArrayList::new));
        List<Philosopher> philosophers = new ArrayList<>();
        IntStream.range(0, n).forEach(i ->
                philosophers.add(new ObservedPhilosopher(sticks.get(i), sticks.get((i+1) % n), i, arbiter)));
        run(philosophers);
    }

    public static void main(String[] args) {
        runPhilosophersWithArbiter(5);
        System.out.println("---------");
        runStarvingPhilosophers(5);
    }

    public static void run(List<Philosopher> philosophers){
        philosophers.forEach(Philosopher::start);
        philosophers.forEach(Philosopher::safeJoin);
    }
}