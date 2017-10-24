/**
 * 
 */
package set1;

import javax.xml.bind.DatatypeConverter;
/**
 * @author QQ
 *
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		byte [] bt1 = {'a','1'};
		System.out.println(bt1[0]);
		System.out.println(common.BCAES.XOR(bt1, bt1)[1]);

	}

}
