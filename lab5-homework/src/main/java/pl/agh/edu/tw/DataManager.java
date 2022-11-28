package pl.agh.edu.tw;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
The most important class, resolves data and save results
 */
public class DataManager {
    private Word word = null;
    private Alphabet alphabet = null;
    private final Map<Character, Operation> productions = new HashMap<>();
    private final Map<Integer, Set<Integer>> edges = new HashMap<>();
    private final Set<Integer> rootVerticles = new HashSet<>();

    private final List<Tuple> dependencies = new ArrayList<>();
    private final Set<Tuple> independencies = new HashSet<>();
    private String FNF = "";
    private String graphDot = null;

    public void setWord(String word) {
        this.word = new Word(word);
    }

    public void setAlphabet(String alphabet) {
        this.alphabet = new Alphabet(alphabet);
    }

    public void addProduction(Character letter, String variable, String operation) {
        productions.put(letter, new Operation(variable, operation));
    }

    public void resolveData() throws Exception {
        if (word == null || alphabet == null || productions.isEmpty()) {
            throw new Exception("Data is not initialized properly");
        }

        createDependenciesAndIndependencies();
        createGraph();
        createFNF();
        generateDotGraph();
    }

    public void saveResults(String subname) throws IOException, InterruptedException {
        saveToTxtFile(subname);
        saveGraphDot(subname);
        saveGraphvizGraph(subname);
    }

    private void saveGraphvizGraph(String subname) throws IOException, InterruptedException {
        String cmd = "dot -Tpng graph_" + subname + ".dot -o graph_" + subname + ".png";
        ProcessBuilder processBuilder = new ProcessBuilder();

        if (System.getProperty("os.name").startsWith("Windows"))
            processBuilder.command("cmd.exe", "/c", cmd);
        else
            processBuilder.command("sh", "-c", cmd);

        Process process = processBuilder.start();
        process.waitFor();
    }

    private void saveGraphDot(String subname) throws IOException {
        String cwd = System.getProperty("user.dir");
        Path filePath = Paths.get(cwd, "graph_" + subname + ".dot");

        Files.deleteIfExists(filePath);
        Files.createFile(filePath);

        FileWriter fileWriter = new FileWriter(filePath.toFile(), true);

        fileWriter.write(graphDot);
        fileWriter.close();
    }
    private void saveToTxtFile(String subname) throws IOException {
        String cwd = System.getProperty("user.dir");
        Path filePath = Paths.get(cwd, "results_" + subname + ".txt");

        Files.deleteIfExists(filePath);
        Files.createFile(filePath);

        FileWriter fileWriter = new FileWriter(filePath.toFile(), true);

        fileWriter.write("D = sym{" +
                dependencies.stream().map(Tuple::toString).collect(Collectors.joining(",")) +
                "}" + System.lineSeparator());
        fileWriter.write("I = sym{" +
                independencies.stream().map(Tuple::toString).collect(Collectors.joining(",")) +
                "}" + System.lineSeparator());
        fileWriter.write("FNF([w]) = " + FNF + System.lineSeparator());
        fileWriter.write(graphDot + System.lineSeparator());
        fileWriter.close();
    }

    private void generateDotGraph() {
        String result = "";
        result += "digraph g{" + System.lineSeparator();
        for (Integer key: edges.keySet()){
            for (Integer edgeTo: edges.get(key)){
                result += key + " -> " + edgeTo + System.lineSeparator();
            }
        }

        Map<Integer, Character> lettersById = word.getLettersById();
        for (Integer key: lettersById.keySet()){
            result += key + "[label=" + lettersById.get(key) + "]" + System.lineSeparator();
        }

        result += "}" + System.lineSeparator();
        graphDot = result;
    }

    private void createFNF(){
        addFNFClass(rootVerticles);
        addNewFNFClass(rootVerticles, new HashSet<>());
    }

    private void addFNFClass(Set <Integer> verticles) {
        FNF += "(";
        verticles.forEach(v -> FNF += word.getLettersById().get(v) + ",");
        FNF = FNF.substring(0, FNF.length() - 1) + ")";
    }

    private void addNewFNFClass(Set<Integer> roots, Set <Integer> movedToNextClass){
        if (roots.isEmpty())
            return;

        Set<Integer> potentialFNFClassElements = new HashSet<>(movedToNextClass);
        Set<Integer> newPotentialFNFClassElements = new HashSet<>();

        roots.forEach(v -> {
            if (edges.containsKey(v))
                potentialFNFClassElements.addAll(edges.get(v));
        });

        potentialFNFClassElements.forEach(v1 -> {
            potentialFNFClassElements.forEach(v2 -> {
                if (edges.containsKey(v1) && edges.get(v1).contains(v2)){
                    newPotentialFNFClassElements.add(v2);
                }
            });
        });

        potentialFNFClassElements.removeAll(newPotentialFNFClassElements);
        addFNFClass(potentialFNFClassElements);
        addNewFNFClass(potentialFNFClassElements, newPotentialFNFClassElements);
    }
    private void createGraph() {
        Map<Integer, Character> lettersById = word.getLettersById();
        IntStream.range(0, lettersById.size()).forEach(i -> {
            boolean rootVerticle = IntStream.range(0, i).noneMatch(v -> areDependent(v, i));

            if (rootVerticle)
                rootVerticles.add(i);
            else
                rootVerticles.forEach(v -> addEdgeToLastRootVertex(i, v));
        });
    }

    private void createDependenciesAndIndependencies() {
        alphabet.getPairTuples().forEach(tuple -> {
            if (productions.get(tuple.getCharacter1()).isDepend(productions.get(tuple.getCharacter2())))
                dependencies.add(tuple);
            else
                independencies.add(tuple);
        });
    }

    private boolean addEdgeToLastRootVertex(int currentID, int rootID) {
        if (!edges.containsKey(rootID)) {
            if (areDependent(currentID, rootID)) {
                edges.put(rootID, new HashSet<>(Set.of(currentID)));
                return true;
            }
            else
                return false;
        }


        boolean added = false;
        for (Integer v : edges.get(rootID)) {
            added = addEdgeToLastRootVertex(currentID, v) || added;
        }


        if (!added && areDependent(currentID, rootID)) {
            edges.get(rootID).add(currentID);
            return true;
        }

        return added;
    }

    private boolean areDependent(int id1, int id2) {
        Map<Integer, Character> lettersById = word.getLettersById();

        return dependencies.contains(new Tuple(lettersById.get(id1), lettersById.get(id2)));
    }

    @Override
    public String toString() {
        return "DataManager{" +
                "\nword=" + word +
                "\nalphabet=" + alphabet +
                "\nproductions=" + productions +
                "\nedges=" + edges +
                "\nrootVerticles=" + rootVerticles +
                "\ndependencies=sym" + dependencies +
                "\nindependencies=sym" + independencies +
                "\nFNF=" + FNF +
                "\n}";
    }
}
