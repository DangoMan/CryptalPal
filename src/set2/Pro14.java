package set2;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import common.BCAES;

public class Pro14 {
	static byte [] randomprefix = {-107,35,-119,38,46,82,21,115,-121,-110,-52,82,-114,-122,-11,43,-43,57,12,0,108,89,-105,-29,-58,-2,56,89,106,4,-117,20,-99,-31};
	static byte [] key = {-55,-122,84,-13,-94,80,-108,-25,44,-30,64,-67,-59,-85,-6,-113};
	static byte [] cipherbyte = "Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkgaGFpciBjYW4gYmxvdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBqdXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUgYnkK".getBytes();


	public static byte[][] AES_128_ECB(byte[] headbyte) throws Exception{


		//some idiot decide it would be a good idea not include append to the library (I could be wrong)


		byte cipherbt[] = new byte[randomprefix.length+headbyte.length+cipherbyte.length];

		//Well, this bothers me
		int i = 0;
		for(;i<randomprefix.length;i++) {
			cipherbt[i] = randomprefix[i];
		}
		for(;i<headbyte.length+randomprefix.length;i++) {
			cipherbt[i] = headbyte[i-randomprefix.length];
		}
		for(;i<headbyte.length+cipherbyte.length+randomprefix.length;i++) {
			cipherbt[i] = cipherbyte[i-headbyte.length-randomprefix.length];
		}

		return BCAES.aesECB(key, BCAES.Base64blockdecomp(cipherbt,16,1), true);
	}

	//step one, a bit slacky, but since my AES ONLY allows a single block of 128....
	public static void blocksize() throws Exception {
		byte pre[][];
		byte cur[][];

		pre = AES_128_ECB("".getBytes());
		cur = AES_128_ECB("A".getBytes());

		String prefix1add = "A";
		int countstep1 = 0;

		while(cur.length - pre.length == 0) {
			pre = AES_128_ECB(prefix1add.getBytes());
			prefix1add +="A";
			cur = AES_128_ECB(prefix1add.getBytes());
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

	//checking consecutive blocks
	public static int checkconc(byte[][] codeblock) {
		int counter = 0;

		for(int i =0; i< codeblock.length-1;i++) {
			if (Arrays.equals(codeblock[i],codeblock[i+1])) {
				return i;
			}
		}
		return -1; 

	}

	public static void main(String args[]) throws Exception {
		//blocksize(cipherbyte);
		//currently 14 16 byte blocks, and it takes 6 extra bytes to "push it to the next block"


		//skipping the CBC check
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

		//checking where the cipher block is and where to prepend 
		byte cur[][];

		String prefix1add = "AAAAAAAAAAAAAAAA";
		cur = AES_128_ECB(prefix1add.getBytes());

		int sameloc = checkconc(cur);

		//
		while(sameloc == -1) {
			prefix1add+="A";
			cur = AES_128_ECB(prefix1add.getBytes());

			sameloc = checkconc(cur);
		}
		//yeah I am being lazy since prefix or subfix might contain conce block
		//Just have a code block to see what change does the same thing
		System.out.println(prefix1add.length());
		System.out.println(sameloc);
		//46 to create a double block = 30 to create a single = pad 14 character to the thing
		//block 3 is where it starts
		
		
		//this is out of lazyness, since java dynamic array can take another hour that is not worth it 
		//TODO add the padbefore to every AES call
		byte[] solution = new byte[cipherbyte.length];

		int pad = 15;
		int padloc = 4;
		String padbefore = "AAAAAAAAAAAAAA";

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
				ECBcodes[i+128] = AES_128_ECB(dicblock)[0];
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

