package common;

import java.util.Arrays;

public class methodtest {

	public static void main(String[] args) {
		String hex = "9908B0DF";
		System.out.println(Integer.parseUnsignedInt("6C078965", 16));
		System.out.println(Integer.toBinaryString(-1));
		System.out.println(Integer.toBinaryString(Integer.MIN_VALUE));
		System.out.println(Integer.toBinaryString((-1) >>> 30));
		System.out.println(Integer.toBinaryString(Integer.MAX_VALUE));
		System.out.println(Integer.toBinaryString((int) Math.pow(2, 29)) );
		System.out.println((Integer.MIN_VALUE +1) << 1);
		
		long x = ((long) (Integer.MAX_VALUE))+1;
		System.out.println(Integer.toBinaryString( (Integer.MAX_VALUE)));
		System.out.println(Integer.toBinaryString((int)x));
		
		System.out.println(Long.toBinaryString(Long.parseLong("80000000",16)));
		System.out.println(Long.toBinaryString(Long.parseLong("7FFFFFFF",16)));
		
	}

}
