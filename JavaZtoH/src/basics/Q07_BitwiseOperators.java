package basics;

public class Q07_BitwiseOperators {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        // Bitwise AND (&)
        int a = 5;      // binary: 0101
        int b = 3;      // binary: 0011
        int andResult = a & b; // binary: 0001 (decimal: 1)
        System.out.println("Bitwise AND: " + andResult);

        // Bitwise OR (|)
        int orResult = a | b;  // binary: 0111 (decimal: 7)
        System.out.println("Bitwise OR: " + orResult);

        // Bitwise XOR (^)
        int xorResult = a ^ b; // binary: 0110 (decimal: 6)
        System.out.println("Bitwise XOR: " + xorResult);

        // Bitwise NOT (~)
        int notResultA = ~a;  // binary: 11111111111111111111111111111010 (decimal: -6)
        int notResultB = ~b;  // binary: 11111111111111111111111111111100 (decimal: -4)
        System.out.println("Bitwise NOT of a: " + notResultA);
        System.out.println("Bitwise NOT of b: " + notResultB);

        // Left Shift (<<)
        int leftShiftResultA = a << 1; // binary: 1010 (decimal: 10)
        int leftShiftResultB = b << 2; // binary: 1100 (decimal: 12)
        System.out.println("Left Shift of a: " + leftShiftResultA);
        System.out.println("Left Shift of b: " + leftShiftResultB);

        // Right Shift (>>)
        int rightShiftResultA = a >> 1; // binary: 0010 (decimal: 2)
        int rightShiftResultB = b >> 1; // binary: 0001 (decimal: 1)
        System.out.println("Right Shift of a: " + rightShiftResultA);
        System.out.println("Right Shift of b: " + rightShiftResultB);

        // Unsigned Right Shift (>>>)
        int unsignedRightShiftResultA = a >>> 1; // binary: 0010 (decimal: 2)
        int unsignedRightShiftResultB = b >>> 1; // binary: 0001 (decimal: 1)
        System.out.println("Unsigned Right Shift of a: " + unsignedRightShiftResultA);
        System.out.println("Unsigned Right Shift of b: " + unsignedRightShiftResultB);

	}

}
