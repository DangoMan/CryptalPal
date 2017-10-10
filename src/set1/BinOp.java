package set1;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.xml.bind.DatatypeConverter;
/*
 * Author: Junqiao Randy Lin
 * Binary encoding and decoding, and as well as binary operation for the crypto challenge
 * I am trying to implement everything by hand for partice purpose, please don't get too angry because some of my coding
 * (I know the ifs, Like I know, but.... just leave it)
 * 
 */


public class BinOp {


	//this encodes hex64 to String
	//Style I know
	public static String  encodehex(String str){
		//self learning note, hex = 16 = 2^4, 64 = 2^6
		String rawbin ="";
		//System.out.println(str);
		for(int i = 0; i< str.length(); i += 4){
			int counter = 0;
			for(int j = 0; j < 4; j++){
				if(str.charAt(i+j) == '1'){
					counter += (int) Math.pow(2, (3-j));
				}
			}
			//System.out.println(counter);

			if(counter < 10){
				rawbin +=  (char) (counter + '0');
			}
			else {
				rawbin +=  (char) (counter-10 + 'a');
			}


		}
		return rawbin;
	}

	public static String  decodehex(String str){
		//self learning note, hex = 16 = 2^4, 64 = 2^6
		String rawbin ="";

		for(int i = 0; i< str.length(); i++){
			char h = str.charAt(i);
			if(h == '0'){
				rawbin += "0000";
			}
			else if(h == '1'){
				rawbin += "0001";
			}
			else if(h == '2'){
				rawbin += "0010";
			}
			else if(h == '3'){
				rawbin += "0011";
			}
			else if(h == '4'){
				rawbin += "0100";
			}
			else if(h == '5'){
				rawbin += "0101";
			}
			else if(h == '6'){
				rawbin += "0110";
			}
			else if(h == '7'){
				rawbin += "0111";
			}
			else if(h == '8'){
				rawbin += "1000";
			}
			else if(h == '9'){
				rawbin += "1001";
			}
			else if(h == 'a'){
				rawbin += "1010";
			}
			else if(h == 'b'){
				rawbin += "1011";
			}
			else if(h == 'c'){
				rawbin += "1100";
			}
			else if(h == 'd'){
				rawbin += "1101";
			}
			else if(h == 'e'){
				rawbin += "1110";
			}
			else if(h == 'f'){
				rawbin += "1111";
			}
		}
		return rawbin;
	}



	//this encodes binary to base64
	public static String  encodebase64(String str)  {

		char[] inChar = str.toCharArray();
		String result ="";
		for(int i = 0; i<inChar.length; i += 6){
			int counter = 0;
			for(int j = 0; j < 6; j++){
				if(i+j < inChar.length){
					if(inChar[i+j] == '1'){
						counter += (int) Math.pow(2, (5-j));
					}
				}
			}


			if(counter<26){
				result +=  (char) (counter + 'A');
			}
			else if(counter < 52 ){
				result += (char) (counter-26 + 'a');
			}
			else if(counter < 62){
				result += (char)(counter-52 + '0');
			}
			else if(counter == 62){
				result += '+';
			}
			else{
				result += '/';
			}
		}

		return result;
	}

	public static String  decodebase64(String str){
		String result = "";

		for(int i = 0; i<str.length();i++){
			char code = str.charAt(i);
			int counter = 0;

			//check character
			if (code >= 'A' && code <= 'Z'){
				counter = code - 'A';
			}
			else if (code >= 'a' && code <= 'z'){
				counter = code - 'a' + 26;
			}

			else if(code >= '0' && code <= '9' ){
				counter = code - '0' +52;
			}

			else if(code =='+'){
				counter = 62;
			}

			else counter = 63;

			//translate to binary
			for(int j = 0; j < 6; j++){
				if(counter >=  Math.pow(2, (5-j))){
					counter -= (int) Math.pow(2, (5-j));
					result += '1';
				}

				else {
					result += '0';
				}
			}
		}
		return result;


	}
	//takes two binary string bin1 and bin2, return when both XOR together 
	public static String XORbin(String bin1, String bin2){
		String result = "";
		for(int i= 0; i<bin1.length();i++){
			if(bin1.charAt(i) == bin2.charAt(i)){
				result += '0';
			}
			else{
				result += '1';
			}
		}
		return result;
	}

	//takes a binary String and a character and XOR the binary String by the XOR
	public static String XORbinsingle(String bin1, String c){
		String result = "";


		for(int i= 0; i<bin1.length();i+= c.length()){
			for(int j =0; j <c.length()&& i+j < bin1.length() ; j++){
				if(bin1.charAt(i+j) == c.charAt(j)){
					result += '0';
				}
				else{
					result += '1';
				}
			}
		}
		return result;
	}


	//convert bewteen ascii value to stuff
	public static String asciiToHex(String asciiValue){
		char[] chars = asciiValue.toCharArray();
		StringBuffer hex = new StringBuffer();
		//System.out.println((int) chars[1]);
		for (int i = 0; i < chars.length; i++)
		{
			hex.append(Integer.toHexString((int) chars[i]));
		}
		return hex.toString();
	}


	public static String hexToASCII(String hexValue){
		StringBuilder output = new StringBuilder("");
		for (int i = 0; i < hexValue.length(); i += 2)
		{
			String str = hexValue.substring(i, i + 2);
			output.append((char) Integer.parseInt(str, 16));
		}
		return output.toString();
	}

	//lol
	public static String asciiToBin(String asciiValue){
		return decodehex(asciiToHex(asciiValue));
	}

	public static String binToASCII (String hexValue){
		return hexToASCII(encodehex(hexValue));
	}


	public static String XORhex(String str1, String str2){
		return BinOp.encodehex(BinOp.XORbin(BinOp.decodehex(str1), BinOp.decodehex(str2)));
	}

	public static int Hammingscore(String str1, String str2){
		String res = XORbin(str1, str2);
		int counter = 0;

		for(int i = 0; i<res.length();i++){
			if(res.charAt(i) == '1'){
				counter ++;
			}
		}
		return counter;
	}



}
