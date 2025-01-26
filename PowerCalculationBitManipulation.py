def power_bitwise(base, exponent):
        if exponent == 0:
        return 1
        if exponent < 0:
base = 1 / base
        exponent = -exponent
result = 1
        while exponent > 0:
        if exponent % 2 == 1:  # If exponent is odd
result *= base
base *= base
        exponent //= 2  # Divide exponent by 2
    return result

# Example Usage
print(power_bitwise(2, 3))  # Output: 8
print(power_bitwise(2, -3)) # Output: 0.125