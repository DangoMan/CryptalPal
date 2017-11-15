package set3;
import java.util.Base64;

import common.BCAES;

public class pro18 {
	public static void main(String args[]) throws Exception {
		//Note to self, BASE 64 conversion
		byte [][] cipherblock = BCAES.Base64blockdecomp(Base64.getDecoder().decode("L77na/nrFsKvynd6HzOoG7GHTLXsTVu9qvY/2syLXzhPweyyMTJULu/6/kXX0KSvoOLSFQ=="), 16, 0);
		byte[] nonce = {0,0,0,0,0,0,0,0};
		byte [][] txtblock = BCAES.aesCTR("YELLOW SUBMARINE".getBytes(), nonce, cipherblock);
		String sol = "";
		for(int i = 0; i<txtblock.length;i++) {
			sol += new String(txtblock[i]);
		}
		System.out.println(sol);
	}
}
