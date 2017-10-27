package common;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

//I am not good with names :(
public class Gen12stuff {
	public static void main(String args[]) throws NoSuchAlgorithmException {
		byte[] key = new byte[16] ;
		SecureRandom.getInstanceStrong().nextBytes(key);
		System.out.print("{");
		
		for(int i = 0; i< 15;i++) {
			System.out.print(key[i]+ "," );
		}
		
		System.out.print(key[15] + "}");
	}

}
