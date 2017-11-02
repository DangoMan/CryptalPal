package common;


//output, -113. 63
public class Why_I_realize_java_was_bad_for_this_lolololololololol {

	public static void main(String[] args) {
		byte test1 = -113;
		
		System.out.println(test1);
		
		byte [] test = {test1,1,2,3,4};
		test[0] = -113;
		byte test2[] = new String(test).getBytes();
		byte test3 = test2[0];
		System.out.println(test3);

	}

}
