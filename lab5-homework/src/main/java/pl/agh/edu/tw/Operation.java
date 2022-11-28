package pl.agh.edu.tw;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Operation {
    private final Character variable;
    private final String value;
    private Set<Character> dependencies;

    public Operation(String letter, String value) {
        this.variable = letter.charAt(0);
        this.value = value;
        createDependenciesSet();
    }

    private void createDependenciesSet(){
        dependencies = value.chars()
                .mapToObj(e -> (char)e)
                .filter(Character::isLetter)
                .collect(Collectors.toCollection(HashSet::new));
    }

    public Character getVariable() {
        return variable;
    }

    public Set<Character> getDependencies() {
        return dependencies;
    }

    public boolean isDepend(Operation operation){
        return dependencies.contains(operation.variable) ||
                operation.variable.equals(variable) ||
                operation.dependencies.contains(variable);
    }

    @Override
    public String toString() {
        return "Operation{" +
                "variable='" + variable + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
