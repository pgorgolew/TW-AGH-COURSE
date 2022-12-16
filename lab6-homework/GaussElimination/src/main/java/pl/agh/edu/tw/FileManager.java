package pl.agh.edu.tw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FileManager {
    private int n = -1;
    List<Double> matrixValues = new ArrayList<>();
    List<Double> yValues;

    public Manager getManager(String filename) throws FileNotFoundException {
        Scanner reader = new Scanner(new File(filename));
        int parsedLines = 0;
        while (reader.hasNextLine()) {
            String data = reader.nextLine();
            parseLine(data, parsedLines);
            parsedLines++;
        }
        reader.close();

        return new Manager(new ExtendedMatrix(n, matrixValues, yValues));
    }

    private void parseLine(String line, int parsedLines){
        if (parsedLines == 0){
            n = Integer.parseInt(line);
        }
        else if (parsedLines < n+1){
            matrixValues.addAll(
                    Arrays.stream(line.split(" "))
                          .map(Double::parseDouble)
                          .toList()
            );
        }
        else {
            yValues = Arrays.stream(line.split(" "))
                            .map(Double::parseDouble)
                            .collect(Collectors.toCollection(ArrayList::new));
        }
    }

    public static void save(String content, String filename) throws IOException {
        String cwd = System.getProperty("user.dir");
        Path filePath = Paths.get(cwd, filename);

        Files.deleteIfExists(filePath);
        Files.createFile(filePath);

        FileWriter fileWriter = new FileWriter(filePath.toFile(), true);

        fileWriter.write(content);

        fileWriter.close();
    }
}
