package set1;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import java.security.*;
public class pro7 {

	public static void main(String[] args) throws FileNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		String key = "YELLOW SUBMARINE";

		Scanner sc = new Scanner(new File("C:\\Users\\QQ\\Documents\\GitHub\\Cryptopal\\Cryptalpal\\src\\7.txt"));
		String ciphertext = "";

		while (sc.hasNext()) {
			ciphertext += sc.nextLine();
		}

		//System.out.println(ciphertext);
		byte[] ciphertxt = Base64.getDecoder().decode(ciphertext);
		
		

		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
		String plaintext = "";
		
		byte[][] block = new byte[ciphertxt.length][16];
		
		
		for(int i = 0; i<ciphertxt.length/16;i++) {
			block[i] = Arrays.copyOfRange(ciphertxt,i*16 , i*16+16);
			
		}
		
		
		//change it so it's not cheating :D
		for(int i = 0; i<block.length;i++) {
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			//System.out.println(Base64.getDecoder().decode(str[i]).length);
			//System.out.println();
			
			//doing this block by block
			byte[] plaintextbyte = cipher.doFinal(block[i]);
			plaintext += new String(plaintextbyte);
			skeySpec = new SecretKeySpec(key.getBytes(), "AES");
		}
		System.out.println(plaintext);
	}

}
