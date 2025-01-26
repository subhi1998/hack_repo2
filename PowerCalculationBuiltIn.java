
// Builtin Java lib import power calculation
public class PowerCalculationBuiltIn {
    public static double powerBuiltin(double base, int exponent) {
        return Math.pow(base, exponent);
    }

    public static void main(String[] args) {
        System.out.println(powerBuiltin(2, 3));   // Output: 8.0
        System.out.println(powerBuiltin(2, -3));  // Output: 0.125
    }
}