package pl.agh.edu.tw;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
This class parse input file (like example_file1.txt) and returns dataManger where info is already set (getDataManager)
 */
public class FileManager {
    private final Pattern wordPattern = Pattern.compile("w = (?<word>[a-z]+)");
    private final Pattern alphabetPattern = Pattern.compile("A = \\{(?<alphabetContent>([a-z], )*[a-z])}");
    private final Pattern productionPattern = Pattern.compile("\\((?<letter>[a-z])\\) (?<variable>[a-z]) := (?<operation>.*)");

    public DataManager getDataManager(String filename) throws FileNotFoundException {
        Scanner reader = new Scanner(new File(filename));
        DataManager dataManager = new DataManager();
        while (reader.hasNextLine()) {
            String data = reader.nextLine();
            parseLine(data, dataManager);
        }
        reader.close();

        return dataManager;
    }

    private void parseLine(String line, DataManager dataManager){
        Matcher wordMatcher = wordPattern.matcher(line);
        Matcher alphabetMatcher = alphabetPattern.matcher(line);
        Matcher productionMatcher = productionPattern.matcher(line);

        if (wordMatcher.matches()){
            String word = wordMatcher.group("word");
            dataManager.setWord(word);
        }
        else if (alphabetMatcher.matches()){
            String alphabetContent = alphabetMatcher.group("alphabetContent");
            dataManager.setAlphabet(alphabetContent);
        }
        else if (productionMatcher.matches()){
            String letter = productionMatcher.group("letter");
            String variable = productionMatcher.group("variable");
            String operation = productionMatcher.group("operation");
            dataManager.addProduction(letter.charAt(0), variable, operation);
        }
    }
}
