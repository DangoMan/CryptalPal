package set2;

import java.lang.reflect.Array;
import java.util.Arrays;

public class pro13 {
	public static byte[] key = {-106,-117,-68,26,-121,107,22,-106,-26,3,124,84,29,-50,-61,-95};
	//back in the good old racket days 
	public static  String kvparsingroutine(String str) {
		String sol = "{\n  ";
		for(int i = 0; i<str.length();i++) {
			char curchar = str.charAt(i);
			
			if(curchar == '=') {
				sol += ": \'";
			}
			else if(curchar== '&') {
				sol += "\',\n  ";
			}
			else {
				sol += curchar;
			}
		}
		
		sol += "\'\n}";
		return sol;
		
	}
	
	public static  String profile_for(String str) {
		String parse = "";
		
		//will go with eating for now, but it's the same idea
		for(int i = 0; i<str.length(); i++) {
			if(!(str.charAt(i) == '&' || str.charAt(i) == '=')) {
				parse += str.charAt(i);
			}
			
		}
		
		
		return "email="+parse+"&uid=10&role=user";
		
	}
	
	
	//Java's char interaction is downright weird, so I will only use char for outout
	public static byte[] encodekvparse(String str) throws Exception{
		
		
		byte codeblock [][] = common.BCAES.aesECB(key, common.BCAES.Base64blockdecomp(profile_for(str).getBytes(), 16, 1), true);

		byte[] str_block = new byte[16*codeblock.length];
		for(int i = 0; i<codeblock.length;i++) {
			for(int j = 0; j<16;j++) {
				str_block[i*16 +j] = codeblock[i][j]; 
			}
		}
		
		return str_block;
		
	}
	
	public static String decodekvparse(byte[] str) throws Exception{

		byte codeblock [][] = common.BCAES.aesECB(key, common.BCAES.Base64blockdecomp(str,16,0),false);
		//strip the padding
		int stop = 0;
		while(stop !=15 && codeblock[codeblock.length-1][stop] != (byte) 0 ) stop++;
		
		String retstr="";
		for(int i = 0; i<codeblock.length-1;i++) {
			retstr += new String(codeblock[i]);
		}
		
		retstr += new String(Arrays.copyOfRange(codeblock[codeblock.length-1], 0, stop));
		
		return kvparsingroutine(retstr);
		
	}
	
	//&role=admin
	//email=XXXXXXXXXadmin\x0\x0\x0\x0&uid=10
	//&uid=10&role=
	
	
	public static void main(String args[]) throws Exception {
		 byte[] comp1= "YELLOWPIGSadmin".getBytes();
		 byte[] comp1_ext = new byte[36];
		 
		 for(int i= 0;i<comp1.length;i++) {
			 comp1_ext[i] = comp1[i];
		 }
		 for(int i= comp1.length;i<comp1_ext.length;i++) {
			 comp1_ext[i] = 0;
		 }
		 
		 
		byte[]block1=encodekvparse(new String(comp1_ext));
		byte[]block2=encodekvparse("YELLOWPIGSYP1");
		
		byte[] adminblock = new byte[48];
		
		for(int i = 0; i<32;i++) {
			adminblock[i] = block2[i];
		}
		for(int i = 32; i<48;i++) {
			adminblock[i] = block1[i-16];
		}
		//for(int i = 0; i<16;i++) {
		//	adminblock[i+32] = block1[i+16];
		//}
		
		
		
		//System.out.println(decodekvparse(encodekvparse("testemail=@") ));
		System.out.println(decodekvparse(block2));
		System.out.println(decodekvparse(adminblock));
	}
}
