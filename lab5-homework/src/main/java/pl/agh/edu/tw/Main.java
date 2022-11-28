package pl.agh.edu.tw;

public class Main {
    public static void main(String[] args) throws Exception {
        DataManager dataManager = new FileManager().getDataManager("example_file.txt");
        dataManager.resolveData();
        System.out.println(dataManager);

    }
}