package set2;

import java.util.Arrays;

public class Pro15 {
	public static void main ( String args[]) throws Exception {
		byte[] test1 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".getBytes();
		System.out.println(test1.length);
		byte[][] testarraylst = common.BCAES.Base64blockdecomp(test1, 16, 1);
		
		testarraylst[1][1] = 15;
		
		byte [] sol = common.BCAES.RemovePk7pad(testarraylst);
		
		System.out.println(Arrays.toString(sol));
	}
}
