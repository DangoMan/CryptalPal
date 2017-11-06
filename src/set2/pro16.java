package set2;

public class pro16 {
	
	public static byte[] key ={121,91,-81,-20,39,37,52,-52,52,48,8,-99,-35,-31,-87,-6};
	public static byte[] iv = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	
	public static byte[][] append(String str) throws Exception {
		byte[] prepend = "comment1=cooking%20MCs;userdata=".getBytes();
		byte[] postpend = ";comment2=%20like%20a%20pound%20of%20bacon".getBytes();
		
		str = str.replaceAll(";", "\";\"");
		str = str.replaceAll("=", "\"=\"");
		
		//a long append/block decomposition
		byte plainblock[][] = common.BCAES.Base64blockdecomp(common.BCAES.appendArr( common.BCAES.appendArr(prepend, str.getBytes()),postpend),16,1);
		
		byte[][] cipherblock = common.BCAES.aesCBC(key, plainblock, iv, true);
		
		return cipherblock;
	}
	
	public static boolean isAdmin(byte[][] bt) throws Exception {
		String plaintext = new String(common.BCAES.RemovePk7pad(common.BCAES.aesCBC(key, bt, iv, false)));

		return plaintext.contains(";admin=true");
	}
	
	//easy
	public static void main(String args[]) throws Exception {
		String appendblock = ";admin=true;cm2=";
		byte[][] blockpar = append("ABCDEFGHIJKLMNOP");
		
		blockpar[2] = common.BCAES.XOR(blockpar[2], common.BCAES.XOR(";comment2=%20lik".getBytes(),appendblock.getBytes()));
		
		System.out.println(isAdmin(blockpar));

	}
}
