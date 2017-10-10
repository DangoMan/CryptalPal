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


		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

		cipher.init(Cipher.DECRYPT_MODE, skeySpec);

		byte[] plaintextbyte = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
		String plaintext = new String(plaintextbyte);
		System.out.println(plaintext);
	}

}
