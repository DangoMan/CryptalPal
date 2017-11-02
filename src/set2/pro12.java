package set2;

import java.util.Arrays;
import java.util.Base64;

import common.BCAES;

public class pro12 {

	static byte key[] = {-11,-118,-15,25,38,-92,-84,118,74,-12,91,-69,-63,91,72,-30};

	public static byte[][] AES_128_ECB(byte[] headbyte, byte[] cipher) throws Exception{


		//some idiot decide it would be a good idea not include append to the library (I could be wrong)

		byte cipherbyte[] = new byte[headbyte.length+cipher.length];

		//Well, this bothers me
		int i = 0;
		for(;i<headbyte.length;i++) {
			cipherbyte[i] = headbyte[i];
		}
		for(;i<headbyte.length+cipher.length;i++) {
			cipherbyte[i] = cipher[i-headbyte.length];
		}

		return BCAES.aesECB(key, BCAES.Base64blockdecomp(cipherbyte,16,1), true);


	}
	//step one, a bit slacky, but since my AES ONLY allows a single block of 128....
	public static void blocksize(byte[] cipherbyte) throws Exception {
		byte pre[][];
		byte cur[][];

		pre = AES_128_ECB("".getBytes(),cipherbyte);
		cur = AES_128_ECB("A".getBytes(),cipherbyte);

		String prefix1add = "A";
		int countstep1 = 0;

		while(cur.length - pre.length == 0) {
			pre = AES_128_ECB(prefix1add.getBytes(),cipherbyte);
			prefix1add +="A";
			cur = AES_128_ECB(prefix1add.getBytes(),cipherbyte);
			countstep1 ++;
		}

		System.out.println(pre.length + " " + cur.length + " " + countstep1 + " "+ pre[0].length);
	}

	//step two
	public static int countsameblock(byte[][] codeblock) {
		int counter = 0;

		for(int i =0; i< codeblock.length;i++) {
			for(int j = i+1; j< codeblock.length;j++) {
				if (Arrays.equals(codeblock[i],codeblock[j])) {
					counter++;
				}
			}
		}
		return counter; 

	}

	public static void main(String args[]) throws Exception {
		String str = "Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkgaGFpciBjYW4gYmxvdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBqdXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUgYnkK";
		//no see ;)

		//think I like this name better
		byte cipherbyte [] = Base64.getDecoder().decode(str);

		//blocksize(cipherbyte);
		//currently 9 16 byte blocks, and it takes 6 extra bytes to "push it to the next block"

		//step2 yeah, "totally" CBC
		//		//append 33 bytes infront
		//		String appstr = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + str;
		//		
		//		
		//		byte appstr [] = Base64.getDecoder().decode(str);
		//		
		//		int pre = countsameblock(BCAES.Base64blockdecomp(testbyte,16,1));
		//		
		//		int post =countsameblock (AES_128_ECB("", testbyte));
		//		
		//		if(pre==post) {
		//			System.out.println("Why are we checking ECB again");
		//		}
		//		
		//		else {
		//			System.out.println("Have fun debugging :D");
		//		}
		//		
		//yeah no it't ECB
		
		//Was cheating, now fixed, cause I am no cheater ;)
		//this is out of lazyness, since java dynamic array can take another hour that is not worth it 
		byte[] solution = new byte[cipherbyte.length];

		int pad = 15;
		int padloc = 0;

		byte dicblock[] = new byte[16];
		for(int i = 0; i< 15;i++) {
			dicblock[i] = 0;
		}


		//first block
		for(int j = 0; j<cipherbyte.length;j++) {
			byte dicblockbase[] = new byte[pad];

			//Initialing prepending block
			for(int i = 0; i< pad;i++) {
				dicblockbase[i] = 0;
			}


			//dictionary code
			byte ECBcodes [][] = new byte[256][16];
			dicblock[15] = Byte.MIN_VALUE;


			//creating an existing dictionary
			for(int i = Byte.MIN_VALUE; i<Byte.MAX_VALUE;i++) {
				ECBcodes[i+128] = AES_128_ECB(dicblock, cipherbyte)[0];
				dicblock[15] ++;
			}


			byte[][] bt = AES_128_ECB(dicblockbase,cipherbyte);

			for(int i = Byte.MIN_VALUE; i< Byte.MAX_VALUE;i++) {
				if(Arrays.equals(bt[padloc], ECBcodes[i+128])) {
					solution[j] = (byte) i;
					break;
				}
			}
			


			for(int i = 0; i< 14;i++) {
				dicblock[i] = dicblock[i+1];
			}

			dicblock[14] = solution[j];
			
			//reset pad length
			pad --;
			if (pad == -1) {
				pad = 15;
				padloc++;
			}

		}
		System.out.println(new String(solution));

		//THIS WORKS :D
		System.out.println(new String(Base64.getDecoder().decode(str)));
	}
}