package main;

import lombok.AllArgsConstructor;

import java.io.InputStream;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.IntStream;

@AllArgsConstructor
public class IntegralCalculator {
    private double a;
    private double b;
    private int n;
    DoubleUnaryOperator f;

    public double calculate() {
        double h = (b-a)/n;
        return IntStream.range(0, n).mapToDouble(i -> a + i * h).map(f).sum() * h;
    }
}
