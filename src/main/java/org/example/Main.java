package org.example;


import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final String[] texts = new String[100_000];

    public static void main(String[] args) throws InterruptedException {

        long start = System.currentTimeMillis();

        Random random = ThreadLocalRandom.current();
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread[] threads = new Thread[3];

        AtomicInteger integer3 = new AtomicInteger(0);
        threads[0] = new Thread(() -> count(integer3, 3));

        AtomicInteger integer4 = new AtomicInteger(0);
        threads[1] = new Thread(() -> count(integer4, 4));

        AtomicInteger integer5 = new AtomicInteger(0);
        threads[2] = new Thread(() -> count(integer5, 5));

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.printf("Красивых слов с длиной 3: %s шт\n", integer3.get());
        System.out.printf("Красивых слов с длиной 4: %s шт\n", integer4.get());
        System.out.printf("Красивых слов с длиной 5: %s шт\n", integer5.get());

        long finish = System.currentTimeMillis();
        System.out.println("Время: " + (finish - start));
    }

    private static void count(AtomicInteger integer, int length) {
        Arrays.stream(texts).filter(n -> n.length() == length & n.equals(new StringBuilder(n)
                .reverse().toString())).forEach(n -> integer.incrementAndGet());
    }

    private static String generateText(String letter, int length) {
        Random random = ThreadLocalRandom.current();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(letter.charAt(random.nextInt(letter.length())));
        }
        return builder.toString();
    }
}
