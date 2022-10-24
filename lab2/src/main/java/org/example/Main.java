package org.example;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void testProducerConsument(int producer_num, int consumer_num, int buffer_size){
        Buffer buffer = new Buffer(buffer_size);

        ArrayList<Consumer> consumers = Stream.generate(() -> new Consumer(buffer)).limit(consumer_num)
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Producer> producers = Stream.generate(() -> new Producer(buffer)).limit(producer_num)
                .collect(Collectors.toCollection(ArrayList::new));

        producers.forEach(Producer::start);
        consumers.forEach(Consumer::start);
    }

    public static void main(String[] args) {
        testProducerConsument(1,1,1);
    }
}