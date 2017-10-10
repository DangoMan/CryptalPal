package set1;

public class pro2 {
	public static void main(String args[]){
		String result = BinOp.encodehex(BinOp.XORbin(BinOp.decodehex("1c0111001f010100061a024b53535009181c"), BinOp.decodehex("686974207468652062756c6c277320657965")));
		System.out.println(result.equals("746865206b696420646f6e277420706c6179"));
	}
}
