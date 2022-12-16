package pl.agh.edu.tw.operations;

import pl.agh.edu.tw.ExtendedMatrix;

import java.util.ArrayList;
import java.util.List;

public class C implements Operation, Runnable{
    private final int r;
    private final int j;
    private final int i;
    private final int id;
    private final ExtendedMatrix matrix;
    private double operationBResult;
    private final List<Operation> dependencies = new ArrayList<>();

    public C(int r, int j, int i, int id, ExtendedMatrix matrix) {
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

    public void setOperationBResult(double n) {
        operationBResult = n;
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
        return 2 + 3 * (i-1);
    }

    @Override
    public void run() {
        matrix.setValue(r,j, matrix.getValue(r,j) - operationBResult);
    }

    @Override
    public String toString() {
        return "C{" +
                "r=" + r +
                ", j=" + j +
                ", i=" + i +
                '}';
    }


}
