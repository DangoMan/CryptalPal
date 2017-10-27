package set2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

import common.BCAES;

public class pro10 {
	public static void main(String args[]) throws Exception {
		Scanner sc = new Scanner(new File("C:\\Users\\QQ\\Documents\\GitHub\\Cryptopal\\Cryptalpal\\src\\10.txt"));
		
		String key = "YELLOW SUBMARINE";
		
		String ciphertext = "";

		while (sc.hasNext()) {
			ciphertext += sc.nextLine();
		}

		//System.out.println(ciphertext);
		byte[][] block = BCAES.Base64blockdecomp( Base64.getDecoder().decode(ciphertext),16,0);
		byte[] vect = new byte[16];
		
		for(int i = 0; i< 16; i++) {
			vect[i] = 0;
		}
		
		String plaintext = "";
		
		for(int i = 0; i<block.length;i++) {
			
			byte[] plainblock = common.BCAES.aesD(key.getBytes(), block[i]);
			
			String blocktext = new String(common.BCAES.XOR(plainblock, vect));
			
			plaintext += blocktext;
			
			vect = block[i];
		}
		
		System.out.println(plaintext);

	}
}
