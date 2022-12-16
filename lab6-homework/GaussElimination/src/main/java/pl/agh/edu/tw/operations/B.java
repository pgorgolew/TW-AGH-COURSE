package pl.agh.edu.tw.operations;

import pl.agh.edu.tw.ExtendedMatrix;

import java.util.ArrayList;
import java.util.List;

public class B implements Operation, Runnable{
    private final int r;
    private final int j;
    private final int i;
    private final int id;
    private final ExtendedMatrix matrix;
    private double operationAResult;
    private final List<Operation> dependencies = new ArrayList<>();

    public B(int r, int j, int i, int id, ExtendedMatrix matrix) {
        this.r = r;
        this.j = j;
        this.i = i;
        this.id = id;
        this.matrix = matrix;
    }

    public int getR() {
        return r;
    }

    public int getJ() {
        return j;
    }

    public int getI() {
        return i;
    }

    public void setOperationAResult(double operationAResult) {
        this.operationAResult = operationAResult;
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
        return 1 + 3 * (i-1);
    }


    @Override
    public void run() {
        double n = operationAResult * matrix.getValue(i,j);
        dependencies.stream().map(c -> (C) c).forEach(c -> c.setOperationBResult(n));
    }

    @Override
    public String toString() {
        return "B{" +
                "r=" + r +
                ", j=" + j +
                ", i=" + i +
                '}';
    }
}
