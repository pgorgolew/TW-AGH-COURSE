package pl.agh.edu.tw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Word {
    private final Map<Integer, Character> lettersById = new HashMap<>();

    public Word(String word) {
        List<Character> letters = word.chars()
                .mapToObj(e -> (char)e)
                .collect(Collectors.toCollection(ArrayList::new));

        IntStream.range(0, letters.size()).forEach(i -> lettersById.put(i, letters.get(i)));
    }


    public Map<Integer, Character> getLettersById() {
        return lettersById;
    }

    @Override
    public String toString() {
        return "Word{" +
                "lettersById=" + lettersById +
                '}';
    }
}
