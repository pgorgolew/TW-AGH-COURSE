package org.example;

public class Main {
    private static void runWithBadSemaphore(){
        new Race(new BadBinSemaphore()).run(3,3);
    }

    private static void runWithGoodSemaphore(){
        new Race(new BinSemaphore()).run(3,3);
    }

    private static void runGeneralSemaphoreExample(){
        new CountingSemaphoreTest(20, 4);
    }

    public static void main(String[] argv) {
        System.out.println("GOOD BINARY");
        runWithGoodSemaphore();
        System.out.println("\nBAD BINARY");
        runWithBadSemaphore();
        System.out.println("\nCOUNTING/GENERAL SEMAPHORE");
        runGeneralSemaphoreExample();
    }
}
