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


		//no padding
		if(PADTYPE == 0) {
			if(ciphertxt.length%size == 0) {
				block = new byte[ciphertxt.length/size][size];
			}
			else {
				block = new byte[ciphertxt.length/size+1][size];
			}

			for(int i = 0; i<ciphertxt.length/size;i++) {
				block[i] = Arrays.copyOfRange(ciphertxt,i*size , (i+1)*size);
			}

			if(ciphertxt.length%size != 0) {
				//system.out.println((block.length)*(size-1) + "\n");
				
				block[block.length-1] = Arrays.copyOfRange(ciphertxt,(block.length-1)*(size), ciphertxt.length);
			}
			
		}
		
		if(PADTYPE == 1) {

			block = new byte[ciphertxt.length/size+1][size];

			for(int i = 0; i<ciphertxt.length/size;i++) {
				block[i] = Arrays.copyOfRange(ciphertxt,i*size , (i+1)*size);
			}
			
			int i = 0;
			for(;i<ciphertxt.length%size;i++ ) {
				block[block.length-1][i] = ciphertxt[(block.length-1)*size + i ];

			}

			for(; i< size;i++) {
				block[block.length-1][i] = (byte) (size - ciphertxt.length%size); 
			}

		}
		
		//16 byte block
		else if(PADTYPE == 1 ) {
			for(int i = 0;i<size;i++ ) {
				block[block.length-1][i] = (byte) size;

			}
		}

		return block;
	}

	public static byte[] RemovePk7pad(byte input[][]) throws Exception{
		int padlength = input.length;
		int padsize = input[0].length;
		int PK7length = input[input.length-1][input[0].length-1];

		if(PK7length > padsize) {
			throw new Exception("invaild PK7 pading, pading too long");
		}

		for(int i = padsize-1; i>padsize-1-PK7length;i--) {
			if(input[padlength-1][i] != PK7length) {
				throw new Exception("invaild PK7 pading");
			}
		}



		byte returnarr[] = new byte[(padlength)*(padsize) -PK7length ];

		for(int i = 0; i< padlength-1;i++) {
			for(int j = 0;j<padsize;j++) {
				returnarr[i*padsize + j] = input[i][j];
			}
		}
		
		for(int i = 0; i< padsize-PK7length;i++) {
			//System.out.println(returnarr.length+" "+(padlength-1)*(padsize)+i);
			returnarr[(padlength-1)*(padsize)+i] =  input[(padlength-1)][i];

		}

		return returnarr;
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
	
	//key: usual
	//nonce: 8 byte nonce, 
	//length is assume to be a mult of 128 byte
	public static byte[][] aesCTR(byte[] key, byte[] nonce, byte[][] block) throws Exception{
		byte sol[][] = new byte[block.length][block[0].length];
		
		//trying to limit the amount of append
		int nonceloc = nonce.length;
		int blockln = block[0].length;
		
		if(nonceloc >= blockln) {
			throw new Exception("Nonce is too long ");
		}
		
		//note the extra length is offset 
		byte[] noncebyte = Arrays.copyOf(nonce, blockln); 
		
		for (int i = 0; i< block.length;i++) {

			//System.out.println(Arrays.toString(noncebyte));
			//System.out.println(Arrays.toString(aesE(key,noncebyte)));
			sol[i] = XOR(block[i],aesE(key,noncebyte));
			//System.out.println(Arrays.toString(block[i]));
			//System.out.println(Arrays.toString(sol[i]));
			
			//System.out.println(new String(sol[i]));
			//add one to the noncebyte block
			N: for(int j = nonceloc; j<blockln;j++) {
				
				//if a overflow happen
				if(noncebyte[j] == -128) {
					noncebyte[j] = 0;
				}
				
				//else wise exit
				else {
					noncebyte[j] = bpp(noncebyte[j]);

					//System.out.println(bpp(noncebyte[j]));
					//System.out.println(bpp(noncebyte[j]));
					break N;
				}
			}
		}
		
		return sol;
	}
	
	
	//"overload operation for byte ++ (since 01111111 ++ -> 10000000) (127++ = -1)"
	//bytewise ++?
	public static byte bpp(byte bt) {
		if(bt == 127) {
			
			return -1;
		}
		if(bt == -128) {
			return 0;
		}
		
		if (bt < 0) {
			return --bt ;
		}
		
		else return ++bt;
	}


	public static byte[] XOR(byte bt1[], byte bt2[]) {
		byte[] returnbt = new byte[bt1.length];

		for(int i = 0; i< bt1.length;i++) {
			returnbt[i] = (byte) (bt1[i]^bt2[i]);
		}

		return returnbt;
	}


}
