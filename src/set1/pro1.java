package set1;
import java.math.BigInteger;

import set1.BinOp;

public class pro1 {
	public static void main(String args[]){
		String result = BinOp.encodebase64(BinOp.decodehex("49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d"));
		System.out.println(result.equals("SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t"));
	}

}
