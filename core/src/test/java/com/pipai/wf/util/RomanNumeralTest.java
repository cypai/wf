package com.pipai.wf.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.pipai.wf.util.RomanNumerals;

public class RomanNumeralTest {

	@Test
	public void testRomanNumerals() {
		assertTrue(RomanNumerals.romanNumeralify(1).equals("I"));
		assertTrue(RomanNumerals.romanNumeralify(2).equals("II"));
		assertTrue(RomanNumerals.romanNumeralify(3).equals("III"));
		assertTrue(RomanNumerals.romanNumeralify(4).equals("IV"));
		assertTrue(RomanNumerals.romanNumeralify(5).equals("V"));
		assertTrue(RomanNumerals.romanNumeralify(6).equals("VI"));
		assertTrue(RomanNumerals.romanNumeralify(9).equals("IX"));
		assertTrue(RomanNumerals.romanNumeralify(10).equals("X"));
	}

}
