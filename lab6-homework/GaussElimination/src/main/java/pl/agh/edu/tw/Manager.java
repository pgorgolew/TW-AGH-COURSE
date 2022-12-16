package pl.agh.edu.tw;

import pl.agh.edu.tw.operations.A;
import pl.agh.edu.tw.operations.B;
import pl.agh.edu.tw.operations.C;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Manager {
    private final ExtendedMatrix matrix;
    private final int n;
    private final int totalFoatsClasses;
    private final List<A> operationsA = new ArrayList<>();
    private final List<B> operationsB = new ArrayList<>();
    private final List<C> operationsC = new ArrayList<>();
    private final List<List<Runnable>> foatsOperations;
    private final List<String> colors;


    public Manager(ExtendedMatrix matrix) {
        this.matrix = matrix;
        this.n = matrix.getRowsSize();
        this.totalFoatsClasses = 3 * (n - 1);
        this.colors = new ArrayList<>(totalFoatsClasses);
        this.foatsOperations = Stream.generate(() -> new ArrayList<Runnable>())
                .limit(totalFoatsClasses)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void resolveData() throws IOException, InterruptedException {
        createOperationsLists();
        addOperationDependencies();
        generateColorsList();
        createFoatsOperations();
        saveDotGraph();
        runGraphviz();
        runGaussElimination();
        saveMatrixOutput();
    }

    private void saveMatrixOutput() throws IOException {
        StringBuilder builder = new StringBuilder(n + System.lineSeparator());

        List<Double> yValues = new ArrayList<>(n);
        IntStream.rangeClosed(1, n).forEach(row -> {
            List<Double> rowContent = matrix.getRow(row);
            yValues.add(rowContent.remove(n));
            String rowString = rowContent.stream().map(Object::toString).collect(Collectors.joining(" "));
            builder.append(rowString).append(System.lineSeparator());
        });

        String yValuesString = yValues.stream().map(Object::toString).collect(Collectors.joining(" "));
        builder.append(yValuesString).append(System.lineSeparator());

        FileManager.save(builder.toString(), "out.txt");
    }
    private void runGaussElimination(){
        foatsOperations.forEach(operations -> {
            ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            operations.stream().map(Thread::new).forEach(service::execute);
            service.shutdown();
            while (!service.isTerminated()) {

            }
        });

        IntStream.iterate(n, n -> n - 1).limit(n).forEach(this::solveRow);
    }

    private void solveRow(int row){
        IntStream.rangeClosed(row+1, n).forEach(r ->{
            double rValue = matrix.getValue(r, n + 1);
            double rCount = matrix.getValue(row, r);
            double yValue = matrix.getValue(row, n+1);
            matrix.setValue(row, n+1, yValue - rValue * rCount);
            matrix.setValue(row, r, 0.0);
        });

        double yValue = matrix.getValue(row, n+1) / matrix.getValue(row, row);
        matrix.setValue(row, n+1, yValue);
        matrix.setValue(row, row, 1.0);
    }
    private void createFoatsOperations(){
        Stream.of(operationsA, operationsB, operationsC)
                .flatMap(Collection::stream)
                .forEach(o -> foatsOperations.get(o.getFloatClassId()).add(o));
    }
    private void generateColorsList(){
        Random random = new Random();
        IntStream.range(0, totalFoatsClasses).forEach(i -> {
            int r = random.nextInt(50,255);
            int g = random.nextInt(50,255);
            int b = random.nextInt(50,255);
            colors.add("#" + String.format("%02X", r) + String.format("%02X", g) + String.format("%02X", b));
        });
    }
    private void saveDotGraph() throws IOException {
        FileManager.save(getBuildedDotGraph().toString(), "graph.dot");
    }

    private void runGraphviz() throws IOException, InterruptedException {
        String cmd = "dot -Tpng graph.dot -o graph.png";
        ProcessBuilder processBuilder = new ProcessBuilder();

        if (System.getProperty("os.name").startsWith("Windows"))
            processBuilder.command("cmd.exe", "/c", cmd);
        else
            processBuilder.command("sh", "-c", cmd);

        Process process = processBuilder.start();
        process.waitFor();
    }
    private StringBuilder getBuildedDotGraph(){
        StringBuilder dotGraphBuilder = new StringBuilder();
        dotGraphBuilder.append("digraph g{").append(System.lineSeparator());

        Stream.of(operationsA, operationsB, operationsC)
                .flatMap(Collection::stream)
                .forEach(o -> {
                    o.getDependencies().forEach(d -> dotGraphBuilder.append(o.getID())
                                                                    .append(" -> ")
                                                                    .append(d.getID())
                                                                    .append(System.lineSeparator()));
                });

        Stream.of(operationsA, operationsB, operationsC)
                .flatMap(Collection::stream)
                .forEach(o -> dotGraphBuilder.append(o.getID())
                                             .append("[label=\"")
                                             .append(o)
                                             .append("\", fillcolor=\"")
                                             .append(colors.get(o.getFloatClassId()))
                                             .append("\", style=filled]")
                                             .append(System.lineSeparator()));

        dotGraphBuilder.append("}").append(System.lineSeparator());
        return dotGraphBuilder;
    }
    private void createOperationsLists(){
        AtomicInteger operationID = new AtomicInteger();
        IntStream.range(1, n+1).forEach(r -> {
            IntStream.range(1,r).forEach(i -> {
                operationsA.add(new A(r,i, operationID.get(), matrix));
                operationID.getAndIncrement();

                IntStream.range(i, n+2).forEach(j -> {
                    operationsB.add(new B(r,j,i, operationID.get(), matrix));
                    operationID.getAndIncrement();

                    operationsC.add(new C(r,j,i, operationID.get(), matrix));
                    operationID.getAndIncrement();
                });
            });
        });
    }

    private void addOperationDependencies(){
        addS1Dependencies();
        addS2Dependencies();
        addS3Dependencies();
        addS4Dependencies();
        addS5Dependencies();
    }

    private void addS1Dependencies(){
        operationsA.forEach(a -> {
            operationsB.forEach(b ->{
                if (a.getR() == b.getR() && a.getI() == b.getI())
                    a.addDependency(b);
            });
        });
    }

    private void addS2Dependencies(){
        operationsB.forEach(b -> {
            operationsC.forEach(c ->{
                if (b.getR() == c.getR() && b.getJ() == c.getJ() && b.getI() == c.getI())
                    b.addDependency(c);
            });
        });
    }

    private void addS3Dependencies(){
        operationsC.forEach(c -> {
            operationsA.forEach(a ->{
                if (c.getJ() == a.getI() && (c.getR() == a.getR() || c.getR() == a.getI()) && a.getI() == c.getI() + 1)
                    c.addDependency(a);
            });
        });
    }

    private void addS4Dependencies(){
        operationsC.forEach(c -> {
            operationsB.forEach(b ->{
                if (c.getJ() == b.getJ() && c.getR() == b.getI() && b.getI() == c.getI() + 1 && b.getJ() != b.getI())
                    c.addDependency(b);
            });
        });
    }

    private void addS5Dependencies(){
        operationsC.forEach(c1 -> {
            operationsC.forEach(c2 ->{
                if (c1.getR() == c2.getR() && c1.getJ() == c2.getJ() && c2.getI() == c1.getI() + 1 && c1.getJ() != c2.getI())
                    c1.addDependency(c2);
            });
        });
    }
}
