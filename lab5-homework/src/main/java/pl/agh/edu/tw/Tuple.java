package pl.agh.edu.tw;

import java.util.Objects;

/*
This class is used to represent pair of characters
 */
public class Tuple {
    private final Character character1;
    private final Character character2;

    public Tuple(Character character1, Character character2) {
        this.character1 = character1;
        this.character2 = character2;
    }

    public Character getCharacter1() {
        return character1;
    }

    public Character getCharacter2() {
        return character2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple tuple = (Tuple) o;
        return (Objects.equals(character1, tuple.character1) && Objects.equals(character2, tuple.character2)) ||
                (Objects.equals(character1, tuple.character2) && Objects.equals(character2, tuple.character1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(character1, character2) + Objects.hash(character2, character1);
    }

    @Override
    public String toString() {
        return "(" + character1 + ", " + character2 + ')';
    }

}
