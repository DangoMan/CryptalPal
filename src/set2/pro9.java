package set2;

import java.security.InvalidKeyException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

//scrap

public class pro9 {

	public static void main (String args[]) {
		String input ="";
		byte[][] output = common.BCAES.Base64blockdecomp(input.getBytes(),16,1);
	}

}

