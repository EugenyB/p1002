package main;

import java.util.function.DoubleUnaryOperator;

public class RunnableCalculator2 extends RunnableCalculator {
    public RunnableCalculator2(double a, double b, int n, DoubleUnaryOperator f, Main main) {
        super(a, b, n, f, main);
    }

    @Override
    public void run() {
        double v = calculator.calculate();
        main.sendResult2(v);
    }
}
