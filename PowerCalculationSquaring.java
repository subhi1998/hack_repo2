// Squaring power calculation
public class PowerCalculationSquaring {
    public static double powerEfficient(double base, int exponent) {
        if (exponent == 0) {
            return 1; // Base case: any number raised to the power of 0 is 1
        }

        if (exponent < 0) {
            base = 1 / base;  // Handle negative exponents
            exponent = -exponent;
        }

        double halfPower = powerEfficient(base, exponent / 2); // Recursively calculate power for half exponent

        if (exponent % 2 == 0) {
            return halfPower * halfPower; // If exponent is even
        } else {
            return base * halfPower * halfPower; // If exponent is odd
        }
    }

    public static void main(String[] args) {
        System.out.println(powerEfficient(2, 10));   // Output: 1024.0
        System.out.println(powerEfficient(2, -3));   // Output: 0.125
        System.out.println(powerEfficient(3, 5));    // Output: 243.0
    }
}
