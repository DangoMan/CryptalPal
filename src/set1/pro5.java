package set1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class pro5 {
	public static void main (String args[]) throws FileNotFoundException{
		Scanner sc = new Scanner(new FileReader("src/input.txt"));
		int shift = 0;
		String key ="ICE";
		boolean isfirst = true;
		//String content = new Scanner(new File("src/input.txt")).useDelimiter("\\Z").next();

		
		//System.out.println(BinOp.encodehex(BinOp.XORbinsingle(content2, BinOp.asciiToBin(key))));
		
		String acc = "";
		
		while(sc.hasNext() != false){
			String st = BinOp.asciiToBin(sc.nextLine());
			if(shift == 0){
				key = "ICE";
			}
			
			else if(shift == 1){
				key = "CEI";
			}
			else if(shift == 2){
				key = "EIC";
			}
			
			if(isfirst){
				isfirst = false;
			}
			
			else {
				st = "00001010" + st;
			}
			
			
			acc+=st;
			
			shift = (st.length() + shift*8) % 8;
		}
		
		String result = BinOp.encodehex(BinOp.XORbinsingle(acc, BinOp.asciiToBin(key)));
		System.out.println(result);
	}
}
