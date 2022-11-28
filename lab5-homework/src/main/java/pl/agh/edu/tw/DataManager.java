package pl.agh.edu.tw;

import java.util.*;
import java.util.stream.IntStream;

public class DataManager {
    private Word word = null;
    private Alphabet alphabet = null;
    private final Map<Character, Operation> productions = new HashMap<>();
    private final Map<Integer, Set<Integer>> edges = new HashMap<>();
    private final Set<Integer> rootVerticles = new HashSet<>();

    private final Set<Tuple> dependencies = new HashSet<>();
    private final Set<Tuple> independencies = new HashSet<>();
    private String FNF = "";

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

    }

    private void createFNF(){
        saveFNFClass(rootVerticles);
        addNewFNFClass(rootVerticles, new HashSet<>());
    }

    private void saveFNFClass(Set <Integer> verticles) {
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
        saveFNFClass(potentialFNFClassElements);
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

    private boolean areDependent(int currentID, int rootID) {
        Map<Integer, Character> lettersById = word.getLettersById();

        return dependencies.contains(new Tuple(lettersById.get(currentID), lettersById.get(rootID)));
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
