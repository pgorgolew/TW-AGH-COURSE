package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    public static void runStarvingPhilosophers(int n, int iterations){
        List<Stick> sticks = Stream.generate(Stick::new).limit(n).collect(Collectors.toCollection(ArrayList::new));
        List<Philosopher> philosophers = new ArrayList<>();
        IntStream.range(0, n).forEach(i -> philosophers.add(new Philosopher(sticks.get(i), sticks.get((i+1) % n), i, iterations)));
        run(philosophers, "starving_results.txt", iterations, n);
    }

    public static void runPhilosophersWithArbiter(int n, int iterations){
        Arbiter arbiter = new Arbiter(n);
        List<Stick> sticks = Stream.generate(Stick::new).limit(n).collect(Collectors.toCollection(ArrayList::new));
        List<Philosopher> philosophers = new ArrayList<>();
        IntStream.range(0, n).forEach(i ->
                philosophers.add(new ObservedPhilosopher(sticks.get(i), sticks.get((i+1) % n), i, arbiter, iterations)));
        run(philosophers, "arbiter_results.txt", iterations, n);
    }

    public static void main(String[] args) {
        int[] philosophersNum = {5, 10, 15};
        int[] iterationsNum = {50, 100, 150};

        for (int philosophers : philosophersNum) {
            for (int iterations : iterationsNum) {
                runPhilosophersWithArbiter(philosophers, iterations);
                runStarvingPhilosophers(philosophers, iterations);
                System.out.println("DONE WITH PARAMS: iterations=" + iterations + " philosophers=" + philosophers);
            }
        }
    }

    public static void run(List<Philosopher> philosophers, String filename, int iterations, int philosophersNum){
        philosophers.forEach(Philosopher::start);
        philosophers.forEach(Philosopher::safeJoin);

        List<String> results = new ArrayList<>(List.of(
                "--------" + new Date() + "--------",
                "PARAMS: iterations=" + iterations + " philosophersNum=" + philosophersNum
        ));
        philosophers.forEach(p -> results.add(p.getResults()));

        try {
            saveResults(results, filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveResults(List<String> sentences, String filename) throws IOException {
        String cwd = System.getProperty("user.dir");
        Path filePath = Paths.get(cwd, filename);

        if (!Files.exists(filePath))
            Files.createFile(filePath);

        FileWriter fileWriter = new FileWriter(filePath.toFile(), true);
        for(String sentence: sentences)
            fileWriter.write(sentence + System.lineSeparator());
        fileWriter.write(System.lineSeparator());

        fileWriter.close();
    }
}