package main;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.Callable;
import java.util.function.DoubleUnaryOperator;

public class CallableIntegralCalculator implements Callable<Double> {

    private final IntegralCalculator calculator;

    public CallableIntegralCalculator(double a, double b, int n, DoubleUnaryOperator f) {
        calculator = new IntegralCalculator(a, b, n, f);
    }

    @Override
    public Double call() throws Exception {
        return calculator.calculate();
    }
}
