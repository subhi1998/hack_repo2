//bit manipulation power calculation
public class PowerCalculationBitManipulation {
    public static double powerBitwise(double base, int exponent) {
        if (exponent == 0) return 1;
        if (exponent < 0) {
            base = 1 / base;
            exponent = -exponent;
        }

        double result = 1;
        while (exponent > 0) {
            if ((exponent & 1) == 1) { // Check if exponent is odd
                result *= base;
            }
            base *= base;
            exponent >>= 1; // Divide exponent by 2
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(powerBitwise(2, 3));   // Output: 8.0
        System.out.println(powerBitwise(2, -3));  // Output: 0.125
    }
}
