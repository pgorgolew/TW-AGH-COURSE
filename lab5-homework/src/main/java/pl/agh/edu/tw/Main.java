package pl.agh.edu.tw;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        for (String subname: List.of("1", "2")){
            DataManager dataManager = new FileManager().getDataManager("example_file" + subname + ".txt");
            dataManager.resolveData();
            dataManager.saveResults(subname);
        }

    }
}