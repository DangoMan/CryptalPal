package set3;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Base64;

import common.BCAES;
import common.BCAES.*;
public class pro17 {
	static byte key[] = {-3,114,68,85,-32,-103,44,47,-119,68,-76,50,45,87,-3,-104};
	//I don't think this matters but I will set this as global variable, it really shouldn't have matter
	static byte iv[] = {-3,114,68,85,-32,-103,44,47,-119,68,-76,50,45,87,-3,-104};

	//Since cipher text is only needed once 
	public static byte[][] encrypt() throws Exception	{
		byte [][] plainlist = new byte[10][1];
		plainlist[0] = Base64.getDecoder().decode("MDAwMDAwTm93IHRoYXQgdGhlIHBhcnR5IGlzIGp1bXBpbmc=");
		plainlist[1] = Base64.getDecoder().decode("MDAwMDAxV2l0aCB0aGUgYmFzcyBraWNrZWQgaW4gYW5kIHRoZSBWZWdhJ3MgYXJlIHB1bXBpbic=");
		plainlist[2] = Base64.getDecoder().decode("MDAwMDAyUXVpY2sgdG8gdGhlIHBvaW50LCB0byB0aGUgcG9pbnQsIG5vIGZha2luZw==");
		plainlist[3] = Base64.getDecoder().decode("MDAwMDAzQ29va2luZyBNQydzIGxpa2UgYSBwb3VuZCBvZiBiYWNvbg==");
		plainlist[4] = Base64.getDecoder().decode("MDAwMDA0QnVybmluZyAnZW0sIGlmIHlvdSBhaW4ndCBxdWljayBhbmQgbmltYmxl");
		plainlist[5] = Base64.getDecoder().decode("MDAwMDA1SSBnbyBjcmF6eSB3aGVuIEkgaGVhciBhIGN5bWJhbA==");
		plainlist[6] = Base64.getDecoder().decode("MDAwMDA2QW5kIGEgaGlnaCBoYXQgd2l0aCBhIHNvdXBlZCB1cCB0ZW1wbw==");
		plainlist[7] = Base64.getDecoder().decode("MDAwMDA3SSdtIG9uIGEgcm9sbCwgaXQncyB0aW1lIHRvIGdvIHNvbG8=");
		plainlist[8] = Base64.getDecoder().decode("MDAwMDA4b2xsaW4nIGluIG15IGZpdmUgcG9pbnQgb2g=");
		plainlist[9] = Base64.getDecoder().decode("MDAwMDA5aXRoIG15IHJhZy10b3AgZG93biBzbyBteSBoYWlyIGNhbiBibG93");

		int index = (int)Math.random() * 10;

		System.out.println( new String(plainlist[index]));

		return BCAES.aesCBC(key, BCAES.Base64blockdecomp(plainlist[index], 16, 1), iv, true);
	}

	public static boolean vaildpad(byte[][] ciphertxt,byte[] iv) throws Exception {
		byte decipherbyte[][] = BCAES.aesCBC(key, ciphertxt, iv, false);
		try {
			BCAES.RemovePk7pad(decipherbyte);
		}

		catch(Exception e) {
			return false;
		}

		return true; 

	}
	
	public static void main(String args[]) throws Exception {
		byte[][] ciphertext = encrypt();

		//this is just vaild pk7 padding insert to the end
		byte baseblockpad[][] = new byte[16][16];


		for(int i = 0; i<16; i++){
			for(int j = 0; j < 16;j++) {
				baseblockpad[i][j] = 0;
			}
		}

		for(int i = 1; i<16; i++){
			for(int j = 15; j > 15-i;j--) {
				baseblockpad[i-1][j] = (byte) i;
			}
		}

		for(int i = 0; i< 16;i++) {
			baseblockpad[15][i] = 16;
		}

		//makes it easier to loop over later
		byte[][] encryptarr = new byte[ciphertext.length+1][16];
		encryptarr[0] = iv;
		for(int i =0;i< ciphertext.length;i++) {
			encryptarr[i+1] = ciphertext[i]; 
		}

		byte solution[][] = new byte[ciphertext.length][16];

		//decrypting each block 
		for(int i = 0;i< ciphertext.length;i++) {

			//keep track of the solution
			byte[] solutionbt = new byte[16];
			for(int j = 0; j<16;j++) {
				solutionbt[j] = 0;
			}


			byte[][] twoblock = new byte[1][16];
			byte[] ivs = new byte[16];

			ivs = encryptarr[i];
			twoblock[0] = encryptarr[i+1];

			//deciphering the next byte
			for(int j = 0; j< 16; j++) {
				N: for(int k = 0; k<256;k++) {
					//OCD to make this work is a bad idea
					if(!(i == ciphertext.length-1 && (Arrays.equals(solutionbt, baseblockpad[j])))) {
						byte[] ivsch= BCAES.XOR(ivs, BCAES.XOR(solutionbt ,baseblockpad[j])); 
						if(vaildpad(twoblock, ivsch)) {
							break N;
						}
					}
					solutionbt[15-j] ++;
				}
			}

			solution[i] = solutionbt;
			
			System.out.println(Arrays.toString(solutionbt));

		}

	}

}
