package set2;

import java.security.InvalidKeyException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class pro9 {
	public static byte[][] Base64blockdecomp(String ciphertext,int padlength){ 
		byte[] ciphertxt = Base64.getDecoder().decode(ciphertext);


		byte[][] block;
		if(ciphertxt.length%padlength == 0) {
			block = new byte[ciphertxt.length/padlength][padlength];
			for(int i = 0; i<ciphertxt.length/padlength;i++) {
				block[i] = Arrays.copyOfRange(ciphertxt,i*padlength , i*padlength+padlength);
			}
		}
		else {
			block = new byte[ciphertxt.length/padlength +1][padlength];

			for(int i = 0; i<ciphertxt.length/padlength;i++) {
				block[i] = Arrays.copyOfRange(ciphertxt,i*padlength , i*padlength+padlength);
			}

			for(int j = 0;j<ciphertxt.length%padlength;j++ ) {
				int index = ciphertxt.length/padlength ;
				index = index * padlength + j;
				block[ciphertxt.length/padlength][j] = ciphertxt [index];
			}

			for(int j = ciphertxt.length%padlength;j<padlength;j++ ) {
				block[ciphertxt.length/padlength][j] = 4; 
			}
		}

		return block;
	}

	public static void main(String args[]) {
		byte block[][] = Base64blockdecomp("ab",16);
		
		for(int i = 0 ;i< block.length;i++) {
			for(int j = 0; j< block[i].length;j++) {
				System.out.print(block[i][j]);
			}
			
			System.out.println();
		}
		
	}
}
