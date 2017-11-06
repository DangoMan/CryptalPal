package set2;

public class Pro15 {
	public static void main ( String args[]) throws Exception {
		byte[] test1 = "AAAAAAAAAAAAAAAAA".getBytes();
		System.out.println(test1.length);
		byte[][] testarraylst = common.BCAES.Base64blockdecomp(test1, 16, 1);
		
		testarraylst[1][1] = 15;
		
		byte [] sol = common.BCAES.RemovePk7pad(testarraylst);
		
		System.out.println(sol.length);
	}
}
