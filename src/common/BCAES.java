package common;

import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class BCAES {
	
	
	public static byte[][] Base64blockdecomp(String ciphertext){ 
		byte[] ciphertxt = Base64.getDecoder().decode(ciphertext);
		
		
		byte[][] block = new byte[ciphertxt.length][16];
		
		
		for(int i = 0; i<ciphertxt.length/16;i++) {
			block[i] = Arrays.copyOfRange(ciphertxt,i*16 , i*16+16);
			
		}
		
		return block;
	}
	
	
	
	//doing one block at a time so it's not cheating ;)
	public static String aes(String key, byte[] block) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		//System.out.println(Base64.getDecoder().decode(str[i]).length);
		//System.out.println();
		
		//doing this block by block
		byte[] plaintextbyte = cipher.doFinal(block);
		
		return new String(plaintextbyte);
	}
}
