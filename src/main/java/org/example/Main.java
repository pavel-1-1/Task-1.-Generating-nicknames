package org.example;


import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

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
        String pattern3 = "ccc|aaa|bbb";
        threads[0] = new Thread(() -> count(integer3, 3, pattern3));

        AtomicInteger integer4 = new AtomicInteger(0);
        String pattern4 = "aaaa|bbbb|cccc|aacc|bbaa|ccaa";
        threads[1] = new Thread(() -> count(integer4, 4, pattern4));

        AtomicInteger integer5 = new AtomicInteger(0);
        String pattern5 = "ccccc|aaaaa|bbbbb|aaacc|babab|acaca";
        threads[2] = new Thread(() -> count(integer5, 5, pattern5));

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

    private static void count(AtomicInteger integer, int length, String pattern) {
        Arrays.stream(texts).filter(n -> n.length() == length).filter(n -> Pattern.matches(
                pattern, n)).forEach(n -> integer.incrementAndGet());
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
