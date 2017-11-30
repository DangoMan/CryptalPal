package set3;
import common.*;
//I don't think this is the right apporach
//ah well, that is the message I see from his site anyways
public class pro22 {
	public static void main (String args[]) {
		
		
		int seed = (int) ( System.currentTimeMillis()/1000L);
		seed += Math.random()*960 + 40;
		long randomval = (int) new common.Mersenne_Twister_19937 (seed).extract_num();
		long startTime = System.currentTimeMillis();
		
		seed += Math.random()*960 + 40;
		
		N: for(int i =0; i<960;i++) {
			long val =  new common.Mersenne_Twister_19937 (seed-i).extract_num();
			if(val == randomval) {
				System.out.println(val);
				break N;
			}
			
			System.out.println(i + ": " + (System.currentTimeMillis()-startTime) );
		}
		
		System.out.println(seed);
	}
}
