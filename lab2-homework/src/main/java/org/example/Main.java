package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    private static void runWithBadSemaphore(){
        new Race(new BadBinSemaphore()).run(3,3);
    }

    private static void runWithGoodSemaphore(){
        new Race(new BinSemaphore()).run(3,3);
    }

    private static void runGeneralSemaphoreExample(){
        new CountersRaces(3).run(3,3);
    }

    public static void main(String argv[]) {
        runWithGoodSemaphore();
        runWithBadSemaphore();
//        runGeneralSemaphoreExample();
    }
}
