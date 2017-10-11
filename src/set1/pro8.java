package set1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class pro8 {
	public static void main(String args[]) throws FileNotFoundException {
		Scanner sc = new Scanner(new File("C:\\Users\\QQ\\Documents\\GitHub\\Cryptopal\\Cryptalpal\\src\\8.txt"));
		
		while(sc.hasNext()) {
			String process = sc.nextLine();
			String str[] = process.split("(?<=\\G.{8})");
			int count = sameblock(str);
			if(count > 0) {
				System.out.println(count);
				for(int i =0; i< str.length;i++) {
					System.out.println(str[i]);
				}
			}
		}
	}
	
	
	public static int sameblock(String[] str) {
		int counter = 0;
		
		for(int i =0; i< str.length;i++) {
			for(int j = i+1; j< str.length;j++) {
				if (str[i].equals(str[j])) {
					counter++;
					System.out.println(str[i]);
				}
			}
		}
		
		System.out.println();
		
		return counter;
	}
}
