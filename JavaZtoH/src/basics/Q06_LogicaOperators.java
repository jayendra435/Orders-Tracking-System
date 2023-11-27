package basics;

public class Q06_LogicaOperators {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean isTrue = true;
        boolean isFalse = false;
        boolean andResult = isTrue && isFalse; // Logical AND
        boolean orResult = isTrue || isFalse; // Logical OR
        boolean notResult = !isTrue; // Logical NOT
        
        System.out.println("\nLogical Operations:");
        System.out.println("Logical AND: " + andResult);
        System.out.println("Logical OR: " + orResult);
        System.out.println("Logical NOT: " + notResult);

	}

}
