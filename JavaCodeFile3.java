
// Using Recursion to Calculate Power
public class JavaCodeFile3 {
    public static double powerRecursive(double base, int exponent) {
        if (exponent == 0) {
            return 1;
        } else if (exponent < 0) {
            return 1 / powerRecursive(base, -exponent);
        } else {
            return base * powerRecursive(base, exponent - 1);
        }
    }

    public static void main(String[] args) {
        System.out.println(powerRecursive(2, 3));   // Output: 8.0
        System.out.println(powerRecursive(2, -3));  // Output: 0.125
    }
}

