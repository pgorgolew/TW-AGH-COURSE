package pl.agh.edu.tw;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Alphabet {
    private final List<Character> alphabetContent;

    public Alphabet(String alphabet){
        alphabet = alphabet.replaceAll(" ", "").replaceAll(",", "");

        //TODO CHECK IF VARIABLES ARE UNIQUE

        alphabetContent = alphabet.chars()
                .mapToObj(e -> (char)e)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<Tuple> getPairTuples(){
        List<Tuple> pairs = new ArrayList<>();
        for (int i=0; i<alphabetContent.size(); i++){
            for (int j=i; j<alphabetContent.size(); j++){
                pairs.add(new Tuple(alphabetContent.get(i), alphabetContent.get(j)));
            }
        }

        return pairs;
    }

    @Override
    public String toString() {
        return "Alphabet{" +
                "alphabetContent=" + alphabetContent +
                '}';
    }
}
