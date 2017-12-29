package org.riverframework;

import java.util.Random;

public class RandomString {

	private static final char[] symbols;

	static {
		StringBuilder tmp = new StringBuilder();
		for (char ch = '0'; ch <= '9'; ++ch)
			tmp.append(ch);
		for (char ch = 'a'; ch <= 'z'; ++ch)
			tmp.append(ch);
		symbols = tmp.toString().toCharArray();
	}   

	private final Random random = new Random(System.currentTimeMillis());

	private final char[] buf;

	public RandomString(int length) {
		if (length < 1)
			throw new IllegalArgumentException("length < 1: " + length);
		buf = new char[length];
	}

	public String nextString() {
		//The first character must be a letter
		buf[0] = symbols[random.nextInt(symbols.length - 10) + 10];
		
		for (int idx = 1; idx < buf.length; ++idx) 
			buf[idx] = symbols[random.nextInt(symbols.length)];
		
		return new String(buf);
	}
}