package pl.agh.edu.tw;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ExtendedMatrix {
    private final List<List<Double>> matrix;

    public ExtendedMatrix(int n, List<Double> matrixValues, List<Double> yValues) {
        if (matrixValues.size() != n * n){
            throw new RuntimeException("Wrong amount of matrixValues!");
        }

        if (yValues.size() != n){
            throw new RuntimeException("Wrong amount of yValues!");
        }

        if (n<1){
            throw new RuntimeException("Size must be positive!");
        }

        matrix = new ArrayList<>(n);
        IntStream.range(0, n).forEach(i -> matrix.add(new ArrayList<>(n + 1)));

        IntStream.range(0, n).forEach(i ->{
            IntStream.range(0, n)
                     .forEach(j -> matrix.get(i).add(matrixValues.get(i * n + j)));
            matrix.get(i).add(yValues.get(i));
        });
    }

    public Double getValue(int row, int col){
        return matrix.get(row - 1).get(col - 1);
    }

    public void setValue(int row, int col, Double value){
        matrix.get(row - 1).set(col - 1, value);
    }

    public int getRowsSize(){
        return matrix.size();
    }

    public List<Double> getRow(int row){
        return matrix.get(row - 1);
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("ExtendedMatrix{" + System.lineSeparator());
        matrix.forEach(row -> builder.append(row.toString()).append(System.lineSeparator()));
        builder.append("}");
        return builder.toString();
    }
}
