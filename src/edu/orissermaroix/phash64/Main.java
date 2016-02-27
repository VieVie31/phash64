package edu.orissermaroix.phash64;

import static edu.orissermaroix.phash64.PHash64.*;

class Main {
	public static void main(String args[]) throws Exception {
		long a = computeHash("res/a.jpg");
		long b = computeHash("res/b.jpg");
		long c = computeHash("res/c.jpg");
		
		System.out.println(hamming(a, a)); //a & a are same
		System.out.println(hamming(a, b)); //a & b are similar
		System.out.println(hamming(a, c)); //a & c are different
		System.out.println(hamming(c, b)); //b & c are different
		System.out.println(hamming(b, c)); //commutativity
	}
}





