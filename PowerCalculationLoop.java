
//Using a Loop to calculate power
public class PowerCalculationLoop {

    public static double powerLoop(double base, int exponent) {
        double result = 1;
        int absExponent = Math.abs(exponent);

        for (int i = 0; i < absExponent; i++) {
            result *= base;
        }

        return (exponent >= 0) ? result : 1 / result;
    }

    public static void main(String[] args) {
        System.out.println(powerLoop(2, 3));   // Output: 8.0
        System.out.println(powerLoop(2, -3));  // Output: 0.125
    }
}
