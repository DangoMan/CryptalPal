package common;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

//I am not good with names :(
public class Genrandbyte {
	public static void main(String args[]) throws NoSuchAlgorithmException {
		
		int prefixlength = (int)( Math.random() * 100);
		
		byte[] key = new byte[prefixlength] ;
		SecureRandom.getInstanceStrong().nextBytes(key);
		System.out.print("{");
		
		for(int i = 0; i< prefixlength -1;i++) {
			System.out.print(key[i]+ "," );
		}
		
		System.out.print(key[prefixlength -1] + "}");
		
		
	}

}
