package org.example;

public class Main {
    public static void runPhilosophersWithBlockPossibility(int n){
        new Table(n).run();
    }

    public static void runPhilospohersWithArbiter(int n){
        Arbiter arbiter = new Arbiter(n);
        new Table(n, arbiter).run();
    }

    public static void main(String[] args) {
        runPhilospohersWithArbiter(5);
        System.out.println("---------");
        runPhilosophersWithBlockPossibility(5);
    }
}