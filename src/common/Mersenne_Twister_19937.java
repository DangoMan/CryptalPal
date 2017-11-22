package common;
//tbh, I still have no idea why this works
//I am guessing at this point with 
public class Mersenne_Twister_19937 {
	long[] intMat  = new long[624];
	int index = 0; 
	long lsd32(long i) {
		return (i&Long.parseLong("FFFFFFFF",16));
	}

	//constructor method
	public Mersenne_Twister_19937(int seed) {
		//computing the initialize the array
		intMat[0] = seed;
		for(int i = 1; i< 624;i++) {
			intMat[i] =lsd32(1812433253L *(intMat[i-1]^intMat[i-1]>>>30)+i);
			//System.out.println(intMat[i]);
		}

		index = 624;

	}


	public long extract_num(){
		if(index>=624) {
			twist();
		}

		long y = intMat[index];
		y = y^y>>>11;
		y = y^y<<7 &2636928640L;
		y = y^y<<15 &4022730752L;
		y = y^y>>>18;
		index ++;

		return lsd32(y);
	}

	private void twist() {
		for(int i = 0; i< 624;i++) {
			long t1 =Long.parseLong("80000000",16);
			long t2 =Long.parseLong("7FFFFFFF",16);
			long y = lsd32((intMat[i]&t1) + (intMat[(i+1)%624]&t2));
//			if(i == 2) {
//				System.out.println(y);
//			}
			intMat[i] = intMat[(i+397)%624]^y>>>1;
//		if(i == 2) {
//			System.out.println(intMat[i]);
//		}
			if(y%2 != 0) {
				intMat[i] = intMat[i] ^ Long.parseLong("9908b0df",16);
			}
			//if(i == 2) {
			//	System.out.println(intMat[i]);
			//}
		}
		index = 0;
	}
}
