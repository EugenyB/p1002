package main;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private double total;
    private int finished;

    private List<String> tNames = new ArrayList<>();

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    private void run() {
        double a = 0;
        double b = Math.PI;
        int n = 1_000_000_000;
//        IntegralCalculator calc = new IntegralCalculator(a, b, n, Math::sin);
//        long start = System.currentTimeMillis();
//        double v = calc.calculate();
//        long finish = System.currentTimeMillis();
//        System.out.println("v = " + v);
//        System.out.println(finish-start);
        long start = System.currentTimeMillis();
        int nThreads = 10;
        double delta = (b-a)/nThreads;
        for (int i = 0; i < nThreads; i++) {
            double ai = a + i * delta;
            double bi = ai + delta;
            new Thread(new RunnableCalculator(ai, bi, n/nThreads, Math::sin, this)).start();
        }

        try {
            synchronized (this) {
                while (finished < nThreads) {
                    wait();
                }
            }
            long finish = System.currentTimeMillis();
            System.out.println(total);
            System.out.println(finish-start);
            tNames.forEach(System.out::println);
        } catch (InterruptedException e) {
            throw new RuntimeException("exception in thread");
        }
    }

    public synchronized void sendResult(double v) {
        total += v;
        finished++;
        tNames.add(Thread.currentThread().getName());
        notify();
    }
}
