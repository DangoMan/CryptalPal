package set1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class pro4 {
	public static void main (String args[]) throws IOException{
		double ratiotest = 5;
		Scanner sc = new Scanner(new FileReader("src/input.txt"));
		while(sc.hasNext() != false){
			String cypher = sc.nextLine();
			String cyphertxt = BinOp.hexToASCII(cypher);
			
			//System.out.println(cyphertxt);
			int counter [] = new int[256];

			for(int i = 0; i< counter.length;i++){
				counter [i] = 0;
			}

			for(int i = 0; i< cyphertxt.length();i++){
				int x = cyphertxt.charAt(i);
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
			
			
			if(counter[max]>= ratiotest){
				String key;				

				key =  Integer.toString(max,2) ;

				while(key.length() != 8){
					key = '0' + key;
				}

				key = BinOp.XORbinsingle(key, "00100000");

				String result = BinOp.hexToASCII(BinOp.encodehex(BinOp.XORbinsingle(BinOp.decodehex(cypher), key)));
				System.out.println(result);
			}


		}
	}
}
