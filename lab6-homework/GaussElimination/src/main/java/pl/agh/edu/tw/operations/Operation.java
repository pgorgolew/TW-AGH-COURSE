package pl.agh.edu.tw.operations;

import pl.agh.edu.tw.ExtendedMatrix;

import java.util.List;

public interface Operation {
    List<Operation> getDependencies();
    void addDependency(Operation op);
    int getID();
    int getFloatClassId();
}
