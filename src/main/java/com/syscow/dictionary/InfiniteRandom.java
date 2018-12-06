package com.syscow.dictionary;

import java.util.Random;

public class InfiniteRandom {
	
	static long count = 0;
	
	public static void main(String[] args) {
		Random rnd = new Random();
		rnd.longs(200_000_000).parallel().forEach(InfiniteRandom::check);
	}
	
	static final void check(long number) {
		count++;
		if (Long.toString(number).indexOf("000000000") > 0) {
			System.out.println(count + " : " + number);
		}
	}
}
