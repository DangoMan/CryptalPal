package common;

import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


//let's be honest, the BinOp class you write a year ago was a piece of shit, yeah
//as with most side projects, structure goes to hell 
public class BCAES {



	//0 = no padding
	//1 = PK7
	public static byte[][] Base64blockdecomp(byte[] ciphertxt,int size,int PADTYPE){ 
		//byte[] ciphertxt = Base64.getDecoder().decode(ciphertext);
		byte[][] block = null;
		if(ciphertxt.length%size == 0) {
			block = new byte[ciphertxt.length/size][size];
		}
		else {
			block = new byte[ciphertxt.length/size+1][size];
		}



		for(int i = 0; i<ciphertxt.length/size;i++) {
			block[i] = Arrays.copyOfRange(ciphertxt,i*size , (i+1)*size);
		}

		if(PADTYPE == 0 && ciphertxt.length%size != 0) {
			block[block.length-1] = Arrays.copyOfRange(ciphertxt,(block.length)*size, ciphertxt.length);
		}
		if(PADTYPE == 1 && ciphertxt.length%size != 0) {

			int i = 0;
			for(;i<ciphertxt.length%size;i++ ) {
				block[block.length-1][i] = ciphertxt[(block.length-1)*size + i ];

			}
			
			for(; i< size;i++) {
				block[block.length-1][i] = (byte) (size - ciphertxt.length%size); 
			}

		}

		return block;
	}



	//doing one block at a time so it's not cheating ;)
	//might look into openssl on a free weekend, 
	public static byte[] aesE(byte[] key, byte[] block) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		//System.out.println(Base64.getDecoder().decode(str[i]).length);
		//System.out.println();

		//doing this block by block
		byte[] plaintextbyte = cipher.doFinal(block);

		//killing the cipher object
		cipher = null;

		return plaintextbyte;
	}

	//think byte[] workes better
	public static byte[] aesD(byte[] key, byte[] block) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		//System.out.println(Base64.getDecoder().decode(str[i]).length);
		//System.out.println();

		//doing this block by block
		byte[] plaintextbyte = cipher.doFinal(block);

		//killing the cipher object
		cipher = null;

		return plaintextbyte;
	}

	//mode = true: encrypt
	//mode = false: decrypt
	public static byte[][] aesCBC(byte[] key, byte[][] block,byte[] ivs, boolean mode) throws Exception {
		byte sol[][] = new byte[block.length][block[0].length];

		//this pokes me a bit 
		for(int i = 0; i<block.length;i++) {
			byte currentb[];

			if(mode) {
				currentb = aesE(key, XOR(ivs ,block[i]));
				ivs = currentb;
				sol[i] = currentb;
			}
			else {
				sol[i] =XOR(ivs, aesD(key, block[i]));
				ivs = block[i];
			}
		}

		return sol;

	}

	//mode = true: encrypt
	//mode = false: decrypt
	public static byte[][] aesECB(byte[] key, byte[][] block,boolean mode) throws Exception {
		byte sol[][] = new byte[block.length][block[0].length];

		//this pokes me a bit 
		for(int i = 0; i<block.length;i++) {
			if(mode) {
				sol[i] = aesE(key,block[i]);
			}
			else {
				sol[i] = aesD(key,block[i]);
			}
		}

		return sol;

	}


	public static byte[] XOR(byte bt1[], byte bt2[]) {
		byte[] returnbt = new byte[bt1.length];

		for(int i = 0; i< bt1.length;i++) {
			returnbt[i] = (byte) (bt1[i]^bt2[i]);
		}

		return returnbt;
	}
}
