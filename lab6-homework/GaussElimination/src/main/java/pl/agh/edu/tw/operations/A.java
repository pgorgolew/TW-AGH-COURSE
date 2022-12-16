package pl.agh.edu.tw.operations;

import pl.agh.edu.tw.ExtendedMatrix;

import java.util.ArrayList;
import java.util.List;

public class A implements Operation, Runnable {
    private final int r;
    private final int i;
    private final int id;
    public final ExtendedMatrix matrix;
    private final List<Operation> dependencies = new ArrayList<>();

    public A(int r, int i, int id, ExtendedMatrix matrix) {
        this.r = r;
        this.i = i;
        this.id = id;
        this.matrix = matrix;
    }

    public int getR() {
        return r;
    }

    public int getI() {
        return i;
    }

    @Override
    public List<Operation> getDependencies() {
        return dependencies;
    }

    @Override
    public void addDependency(Operation op){
        dependencies.add(op);
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public int getFloatClassId() {
        return 3 * (i-1);
    }

    @Override
    public void run() {
        double m = matrix.getValue(r,i) / matrix.getValue(i,i);
        dependencies.stream().map(b -> (B) b).forEach(b -> b.setOperationAResult(m));
    }

    @Override
    public String toString() {
        return "A{" +
                "r=" + r +
                ", i=" + i +
                '}';
    }


}
