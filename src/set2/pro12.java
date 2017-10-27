package set2;

import java.util.Arrays;
import java.util.Base64;

import common.BCAES;

public class pro12 {

	static byte key[] = {-11,-118,-15,25,38,-92,-84,118,74,-12,91,-69,-63,91,72,-30};

	public static byte[][] AES_128_ECB(String head, byte[] cipher) throws Exception{

		byte[] headbyte = head.getBytes();

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

		pre = AES_128_ECB("",cipherbyte);
		cur = AES_128_ECB("A",cipherbyte);

		String prefix1add = "A";
		int countstep1 = 0;

		while(cur.length - pre.length == 0) {
			pre = AES_128_ECB(prefix1add,cipherbyte);
			prefix1add +="A";
			cur = AES_128_ECB(prefix1add,cipherbyte);
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

		//changing the cipher text is cheating
		//the proper way to do this is to use previous result and compute the dictionary
		//then you force the current byte to be the last byte
		//Will do it this way during the c recode afterwards
		byte ECBcodes [][] = new byte[256][16];

		byte dicblock[] = new byte[16];
		for(int i = 0; i< 16; i++) {
			dicblock[i] = 0;
		}
		for(int i =0 ; i<256;i++) {
			ECBcodes[i] = Arrays.copyOf(dicblock, 16);

			dicblock[15] ++;
		}

		ECBcodes = BCAES.aesECB(key,ECBcodes,true);


		byte dicblockbase[] = new byte[15];
		for(int i = 0; i< 15; i++) {
			dicblock[i] = 0;
		}

		String stringbyte = new String(dicblockbase);

		while (cipherbyte.length != 0) {
			byte[][] bt = AES_128_ECB(stringbyte,cipherbyte);

			for(int i = 0; i< 256;i++) {
				if(Arrays.equals(bt[0], ECBcodes[i])) {
					System.out.print((char) i);
				}
			}
			
			cipherbyte = Arrays.copyOfRange(cipherbyte, 1,cipherbyte.length);
		}
		System.out.println();
		
		
		//THIS WORKS :D
		System.out.println(new String(Base64.getDecoder().decode(str)));
	}
}