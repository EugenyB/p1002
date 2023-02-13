package main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    private double total;
    private int finished;

    private Lock lock;
    private Condition condition;

    private List<String> tNames = new ArrayList<>();

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    private void run() {
        double a = 0;
        double b = Math.PI;
        int n = 1_000_000_000;
        int nThreads = 40;
        double delta = (b-a)/nThreads;
        long start = System.currentTimeMillis();
        total = 0;
        IntegralCalculatorThread[] threads = new IntegralCalculatorThread[nThreads];
        for (int i = 0; i < nThreads; i++) {
            double ai = a + i * delta;
            double bi = ai + delta;
            threads[i] = new IntegralCalculatorThread(ai, bi, n/nThreads, Math::sin);
            threads[i].start();
        }
        try {
            for (IntegralCalculatorThread thread : threads) {
                thread.join();
                total += thread.getResult();
            }
            long finish = System.currentTimeMillis();
            System.out.println("result = " + total);
            System.out.println(finish - start);
        } catch (InterruptedException e) {
            System.err.println("Error in thread");
        }
    }

    private void run3() {
        double a = 0;
        double b = Math.PI;
        int n = 1_000_000_000;
        int nThreads = 40;
        double delta = (b-a)/nThreads;
        long start = System.currentTimeMillis();
        total = 0;
        finished = 0;
        for (int i = 0; i < nThreads; i++) {
            double ai = a + i * delta;
            double bi = ai + delta;
            new Thread(new RunnableCalculator2(ai, bi, n/nThreads, Math::sin, this)).start();
        }
        lock = new ReentrantLock();
        condition = lock.newCondition();

        lock.lock();
        try {
            while (finished < nThreads) {
                condition.await();
            }
        } catch (InterruptedException e) {
            System.err.println("Exception in thread");
        } finally {
            lock.unlock();
        }
        long finish = System.currentTimeMillis();
        System.out.println("result = " + total);
        System.out.println(finish - start);
    }

    private void run2() {
        double a = 0;
        double b = Math.PI;
        int n = 1_000_000_000;
        int nThreads = 2000;
        double delta = (b-a)/nThreads;
        long start = System.currentTimeMillis();
        ExecutorService es = Executors.newFixedThreadPool(10);
        List<Future<Double>> futures = new ArrayList<>();
        for (int i = 0; i < nThreads; i++) {
            double ai = a + i * delta;
            double bi = ai + delta;
            CallableIntegralCalculator calculator = new CallableIntegralCalculator(ai, bi, n / nThreads, Math::sin);
            Future<Double> future = es.submit(calculator);
            futures.add(future);
        }

        double total = 0;
        try {
            for (Future<Double> future : futures) {
                total += future.get();
            }
            long finish = System.currentTimeMillis();
            System.out.println("result = " + total);
            System.out.println(finish-start);
            es.shutdown();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error in thread");
        }
    }

    private void run1() {
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

    public void sendResult2(double v) {
        try {
            lock.lock();
            total += v;
            finished++;
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}
