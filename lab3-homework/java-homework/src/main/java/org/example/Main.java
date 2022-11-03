package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    public static HashMap<Integer, HashMap<Integer, HashMap<String, List<String>>>>
            resultsByPhilosophersByIterations = new HashMap<>();

    public static void runStarvingPhilosophers(int n, int iterations){
        List<Stick> sticks = Stream.generate(Stick::new).limit(n).collect(Collectors.toCollection(ArrayList::new));
        List<Philosopher> philosophers = new ArrayList<>();
        IntStream.range(0, n).forEach(i -> philosophers.add(new Philosopher(sticks.get(i), sticks.get((i+1) % n), i, iterations)));
        run(philosophers, "starving", iterations, n);
    }

    public static void runPhilosophersWithArbiter(int n, int iterations){
        Arbiter arbiter = new Arbiter(n);
        List<Stick> sticks = Stream.generate(Stick::new).limit(n).collect(Collectors.toCollection(ArrayList::new));
        List<Philosopher> philosophers = new ArrayList<>();
        IntStream.range(0, n).forEach(i ->
                philosophers.add(new ObservedPhilosopher(sticks.get(i), sticks.get((i+1) % n), i, arbiter, iterations)));
        run(philosophers, "arbiter", iterations, n);
    }

    public static void main(String[] args) throws IOException {
        int[] philosophersNum = {5, 10, 15};
        int[] iterationsNum = {50, 100, 150};

        for (int philosophers : philosophersNum) {
            resultsByPhilosophersByIterations.put(philosophers, new HashMap<>());
            for (int iterations : iterationsNum) {
                resultsByPhilosophersByIterations.get(philosophers).put(iterations, new HashMap<>());
                resultsByPhilosophersByIterations.get(philosophers).get(iterations).put("arbiter", new ArrayList<>());
                resultsByPhilosophersByIterations.get(philosophers).get(iterations).put("starving", new ArrayList<>());
            }
        }

        for (int philosophers : philosophersNum) {
            for (int iterations : iterationsNum) {
                runPhilosophersWithArbiter(philosophers, iterations);
                runStarvingPhilosophers(philosophers, iterations);
                System.out.println("DONE WITH PARAMS: iterations=" + iterations + " philosophers=" + philosophers);
            }
        }

        for (int philosophers : philosophersNum) {
            for (int iterations : iterationsNum) {
                generatePlot(philosophers, iterations, philosophers + "_" + iterations + ".png");
                System.out.println("PLOT DONE FOR PARAMS: iterations=" + iterations + " philosophers=" + philosophers);
            }
        }
    }

    public static void run(List<Philosopher> philosophers, String version, int iterations, int philosophersNum){
        philosophers.forEach(Philosopher::start);
        philosophers.forEach(Philosopher::safeJoin);

        List<String> results = new ArrayList<>(List.of(
                "--------" + new Date() + "--------",
                "PARAMS: iterations=" + iterations + " philosophersNum=" + philosophersNum
        ));
        philosophers.forEach(p -> results.add(p.getResults()));

        try {
            saveResults(results, version + "_results.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        philosophers.forEach(
                p -> resultsByPhilosophersByIterations.get(philosophersNum)
                        .get(iterations)
                        .get(version)
                        .add(p.getAverageWait())
        );

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

    public static void generatePlot(int n, int iterations, String filename) throws IOException {
        Path filePath = Paths.get(System.getProperty("user.dir"), "generate_plots.py");

        List<String> arbiterRes = resultsByPhilosophersByIterations.get(n).get(iterations).get("arbiter");
        List<String> starvingRes = resultsByPhilosophersByIterations.get(n).get(iterations).get("starving");

        Path arbiterPath = Paths.get(System.getProperty("user.dir"), "arbiter.tmp");
        Path starvingPath = Paths.get(System.getProperty("user.dir"), "starving.tmp");

        try {
            Files.createFile(arbiterPath);
            Files.createFile(starvingPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FileWriter fileWriter = new FileWriter(arbiterPath.toFile(), true);
        for(String res: arbiterRes)
            fileWriter.write(res + " ");
        fileWriter.close();

        fileWriter = new FileWriter(starvingPath.toFile(), true);
        for(String res: starvingRes)
            fileWriter.write(res + " ");
        fileWriter.close();


        String command = "python " + filePath + " " + filename + " arbiter starving";
        System.out.println(command);
        try {
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            Files.deleteIfExists(arbiterPath);
            Files.deleteIfExists(starvingPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}