package set1;

public class pro3 {

	public static void main(String[] args) {
		
		String key = BinOp.decodehex(BinOp.asciiToHex("C"));
		String cyphertxt = BinOp.hexToASCII("1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736");
		String result ;
		int counter [] = new int[128];
		
		for(int i = 0; i< counter.length;i++){
			counter [i] = 0;
		}
		
		for(int i = 0; i< cyphertxt.length();i++){
			int x= cyphertxt.charAt(i);
			counter[x] ++;
		}
		
		
		int max = 0; 
		
		for(int i = 0; i< counter.length; i ++){
			if(counter[i] != 0){ 
				if(counter [max] <= counter[i]){
					max = i;
				}
			}
		}
		key =  Integer.toString(max,2) ;
		
		while(key.length() != 8){
			key = '0' + key;
		}
		
		key = BinOp.XORbinsingle(key, "00100000");
		
		result = BinOp.hexToASCII(BinOp.encodehex(BinOp.XORbinsingle(BinOp.decodehex("1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736"), key)));
		System.out.println(result);
		
		//System.out.println(BinOp.encodebase64(BinOp.decodebase64("HelloWorld123+/")));

	}

}


