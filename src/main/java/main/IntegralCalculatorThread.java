package main;

import lombok.Getter;

import java.util.function.DoubleUnaryOperator;


public class IntegralCalculatorThread extends Thread {
    private final IntegralCalculator calculator;

    public IntegralCalculatorThread(double a, double b, int n, DoubleUnaryOperator f) {
        calculator = new IntegralCalculator(a,b,n,f);
    }

    @Override
    public void run() {
        result = calculator.calculate();
    }

    @Getter
    private double result;
}
