package set2;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import common.BCAES;


public class pro11 {
	
	public static byte[][] encryption_oracle(String str) throws Exception {

		//ok I cheated my own rule, but hey this is way cooler
		byte[] key = new byte[16];
		SecureRandom.getInstanceStrong().nextBytes(key);
		
		double enc = Math.random()*2;
		byte[] forwardpad  = new byte[(int) (Math.random()*5 +5)];
		SecureRandom.getInstanceStrong().nextBytes(forwardpad);
		
		byte[] rawbyte = str.getBytes();
		
		byte padtext [] = new  byte[forwardpad.length + rawbyte.length + forwardpad.length];
		int i  = 0;
		for(;i< forwardpad.length;i++) {
			padtext[i] = forwardpad[i];
		}
		for(;i< forwardpad.length+rawbyte.length;i++) {
			padtext[i] = rawbyte[i- forwardpad.length];
		}
		for(;i< padtext.length;i++) {
			padtext[i] = forwardpad[i - forwardpad.length - rawbyte.length];
		}


		
		byte[][] plaintxt = common.BCAES.Base64blockdecomp(padtext, 16, 1);
		
		byte ciphertext[][];
		
		enc = Math.random()*2;

		System.out.println(enc);

		if (enc < 1) {
			byte[] IV = new byte[16];
			SecureRandom.getInstanceStrong().nextBytes(key);
			ciphertext = BCAES.aesCBC(key, plaintxt, IV ,true);
		}
		
		else {
			ciphertext = BCAES.aesECB(key, plaintxt, true);
		}
		
		
		
		return ciphertext;
	}

	
	public static int sameblock(byte[][] str) {
		int counter = 0;
		
		for(int i =0; i< str.length;i++) {
			for(int j = i+1; j< str.length;j++) {
				if (Arrays.equals(str[i],str[j])) {
					counter++;
				}
			}
		}
		
		return counter;
	}
	
	//yeah throw exception, as side projects continues, style goes to hell
	//also I think this is the best way to do the oracle
	public static void main(String args[]) throws Exception {
		
			byte[][] ciphertext = encryption_oracle("                                                                          ");
			
			if(sameblock(ciphertext) > 1) {
				System.out.println("EBC");
			}
			else {
				System.out.println("CBC");
			}
			
		}
}
