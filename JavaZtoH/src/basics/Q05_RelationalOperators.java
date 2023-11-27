package basics;

public class Q05_RelationalOperators {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// relational operators are
		// ==,!=,>,<,<=,>=
		int a = 10;
        int b = 5;
		boolean isEqual = a == b;
        boolean isNotEqual = a != b;
        boolean isGreater = a > b;
        boolean isLess = a < b;
        boolean isGreaterOrEqual = a >= b;
        boolean isLessOrEqual = a <= b;

        System.out.println("Relational Operations:");
        System.out.println("Equal to: " + isEqual);
        System.out.println("Not equal to: " + isNotEqual);
        System.out.println("Greater than: " + isGreater);
        System.out.println("Less than: " + isLess);
        System.out.println("Greater than or equal to: " + isGreaterOrEqual);
        System.out.println("Less than or equal to: " + isLessOrEqual);

	}

}
