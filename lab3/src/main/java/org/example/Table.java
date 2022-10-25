package org.example;

import java.util.ArrayList;
import java.util.List;

public class Table {
    List<Stick> sticks = new ArrayList<>();
    List<Philosopher> philosophers = new ArrayList<>();

    public Table(int n) {
        for (int i=0; i<n; i++)
            sticks.add(new Stick(i, (i+1) % (n-1)));

        for (int i=0; i<n; i++)
            philosophers.add(new Philosopher(sticks.get(i), sticks.get((i+1) % (n-1))));
    }

    public Table(int n, Arbiter arbiter) {
        for (int i=0; i<n; i++)
            sticks.add(new Stick(i, (i+1) % (n-1)));

        for (int i=0; i<n; i++)
            philosophers.add(new Philosopher(sticks.get(i), sticks.get((i+1) % (n-1)), arbiter));
    }

    public void run(){
        philosophers.forEach(Philosopher::start);
        philosophers.forEach(Philosopher::safeJoin);
    }
}
