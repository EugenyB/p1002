package main;

import java.util.function.DoubleUnaryOperator;

public class RunnableCalculator implements Runnable {
    private IntegralCalculator calculator;

    private Main main;

    public RunnableCalculator(double a, double b, int n, DoubleUnaryOperator f, Main main) {
        calculator = new IntegralCalculator(a,b,n,f);
        this.main = main;
    }


    @Override
    public void run() {
        double v = calculator.calculate();
        main.sendResult(v);
    }
}
