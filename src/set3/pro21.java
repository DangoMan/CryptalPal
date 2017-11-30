package set3;
import common.Mersenne_Twister_19937;
public class pro21 {
	public static void main (String args[]) {
		common.Mersenne_Twister_19937 test = new common.Mersenne_Twister_19937(1);
		for(int i = 0 ;i<5; i++) {
			System.out.println(test.extract_num());
		}
	}
}
