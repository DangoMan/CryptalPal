package common;

import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


//let's be honest, the BinOp class you write a year ago was a piece of shit, yeah
//as with most side projects, structure goes to hell 
public class BCAES {

	public static byte [] appendArr ( byte array1[] , byte array2[]){
		
		byte[] returnarr= new byte[array1.length+array2.length];
		int i = 0; 
		for(;i<array1.length;i++) {
			returnarr[i] = array1[i];
		}
		for(;i<returnarr.length;i++) {
			returnarr[i] = array2[i-array1.length];
		}
		return returnarr;
	}

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
	
	public static byte[][] RemovePk7pad(byte input[][]) throws Exception{
		int padlength = input.length;
		int padsize = input[0].length;
		int PK7length = input[input.length-1][input[0].length-1];
		
		if(PK7length >=padsize) {
			throw new Exception("invaild PK7 pading");
		}
		
		for(int i =padsize-1; i>padsize-1-padlength;i--) {
			if(input[padlength-1][i] != padlength) {
				throw new Exception("invaild PK7 pading");
			}
		}
		
		byte temp[] = input[padlength-1];
		
		input[padlength-1] = new byte[PK7length];
		for (int i  = 0; i<padlength;i++) {
			input[padlength-1][i] = temp[i];
		}
		
		return input;
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
