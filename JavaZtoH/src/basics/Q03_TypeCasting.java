package basics;

public class Q03_TypeCasting {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//implicit casting
		// when smaller data types are stored in larger datatypes
		// double is stored in int
		int int_val = 23;
		double double_val = int_val;
		System.out.println("integer value:"+int_val+"   double_value:"+double_val);
		
		// explicit casting
		// when larger datatypes are stored in larger datatypes
		// double stored in int
		// there is a chance of data loss like decimal values can be lost
		double dv = 44.23234;
		int iv= (int)dv;// this is explicit casting
		System.out.println("double_value:"+dv+"   integer value:"+iv);
		
		// remember that all kind of conversion cannot happen directly
		// like we cannot convert string to integer using these techniques
		
		

	}

}
