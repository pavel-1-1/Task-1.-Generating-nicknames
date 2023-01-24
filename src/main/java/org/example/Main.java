package org.example;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static int length3, length4, length5;

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        // счетчик красивых слов длиной 3
        AtomicInteger integer3 = new AtomicInteger(0);
        Thread thread3 = new Thread(() ->
                Arrays.stream(texts).filter(n -> n.length() == 3 & (n.startsWith("aaa") | n.startsWith("ccc")
                        | n.startsWith("bbb") | n.startsWith("aca") | n.startsWith("aba") | n.startsWith("bab") | n.startsWith("bcb")
                        | n.startsWith("cac") | n.startsWith("cbc"))).forEach(n -> counter(integer3)));

        // счетчик красивых слов длиной 4
        AtomicInteger integer4 = new AtomicInteger(0);
        Thread thread4 = new Thread(() ->
                Arrays.stream(texts).filter(n -> n.length() == 4 & (n.startsWith("aaaa") | n.startsWith("cccc")
                        | n.startsWith("bbbb") | n.startsWith("aacc") | n.startsWith("aabb") | n.startsWith("baba") | n.startsWith("bcbc")
                        | n.startsWith("caca") | n.startsWith("ccbb"))).forEach(n -> counter(integer4)));

        // счетчик красивых слов длиной 5
        AtomicInteger integer5 = new AtomicInteger(0);
        Thread thread5 = new Thread(() ->
                Arrays.stream(texts).filter(n -> n.length() == 5 & (n.startsWith("aaaaa") | n.startsWith("ccccc")
                        | n.startsWith("bbbbb") | n.startsWith("aabbb") | n.startsWith("ababa") | n.startsWith("babab") | n.startsWith("bbccc")
                        | n.startsWith("cacac") | n.startsWith("cbcbc"))).forEach(n -> counter(integer5)));

        thread3.start();
        thread4.start();
        thread5.start();
        thread3.join();
        thread4.join();
        thread5.join();

        length3 = integer3.get();
        length4 = integer4.get();
        length5 = integer5.get();


        System.out.printf("Красивых слов с длиной 3: %s шт\n", integer3.get());
        System.out.printf("Красивых слов с длиной 4: %s шт\n", integer4.get());
        System.out.printf("Красивых слов с длиной 5: %s шт\n", integer5.get());

        long finish = System.currentTimeMillis();
        System.out.println("Time: " + (finish - start));
    }

    protected static void counter(AtomicInteger num) {
        num.incrementAndGet();
    }

    protected static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}

// TODO Так красивее но в 2.5 раза медленнее

//public class Main {
//    private static final String[] texts = new String[100_000];
//
//    public static void main(String[] args) throws InterruptedException {
//
//        long start = System.currentTimeMillis();
//
//        Random random = ThreadLocalRandom.current();
//        for (int i = 0; i < texts.length; i++) {
//            texts[i] = generateText("abc", 3 + random.nextInt(3));
//        }
//
//        Thread[] threads = new Thread[3];
//
//        AtomicInteger integer3 = new AtomicInteger(0);
//        String pattern3 = "ccc|aaa|bbb";
//        threads[0] = new Thread(() -> count(integer3, 3, pattern3));
//
//        AtomicInteger integer4 = new AtomicInteger(0);
//        String pattern4 = "aaaa|bbbb|cccc|aacc|bbaa|ccaa";
//        threads[1] = new Thread(() -> count(integer4, 4, pattern4));
//
//        AtomicInteger integer5 = new AtomicInteger(0);
//        String pattern5 = "ccccc|aaaaa|bbbbb|aaacc|babab|acaca";
//        threads[2] = new Thread(() -> count(integer5, 5, pattern5));
//
//        for (Thread thread : threads) {
//            thread.start();
//        }
//
//        for (Thread thread : threads) {
//            thread.join();
//        }
//
//        System.out.printf("Красивых слов с длиной 3: %s шт\n", integer3.get());
//        System.out.printf("Красивых слов с длиной 4: %s шт\n", integer4.get());
//        System.out.printf("Красивых слов с длиной 5: %s шт\n", integer5.get());
//
//        long finish = System.currentTimeMillis();
//        System.out.println("Время: " + (finish - start));
//    }
//
//    private static void count(AtomicInteger integer, int length, String pattern) {
//        Arrays.stream(texts).filter(n -> n.length() == length).filter(n -> Pattern.matches(
//                pattern, n)).forEach(n -> integer.incrementAndGet());
//    }
//
//    private static String generateText(String letter, int length) {
//        Random random = ThreadLocalRandom.current();
//        StringBuilder builder = new StringBuilder();
//        for (int i = 0; i < length; i++) {
//            builder.append(letter.charAt(random.nextInt(letter.length())));
//        }
//        return builder.toString();
//    }
//}
