package pl.agh.edu.tw;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String filename = "in.txt";
        if (args.length != 0){
            filename = args[0];
        }

        Manager manager = new FileManager().getManager(filename);
        manager.resolveData();
    }
}
