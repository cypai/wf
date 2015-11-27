package com.pipai.wf.util;

import org.junit.Assert;
import org.junit.Test;

public class RomanNumeralTest {

	@Test
	public void testRomanNumerals() {
		Assert.assertTrue(RomanNumerals.romanNumeralify(1).equals("I"));
		Assert.assertTrue(RomanNumerals.romanNumeralify(2).equals("II"));
		Assert.assertTrue(RomanNumerals.romanNumeralify(3).equals("III"));
		Assert.assertTrue(RomanNumerals.romanNumeralify(4).equals("IV"));
		Assert.assertTrue(RomanNumerals.romanNumeralify(5).equals("V"));
		Assert.assertTrue(RomanNumerals.romanNumeralify(6).equals("VI"));
		Assert.assertTrue(RomanNumerals.romanNumeralify(9).equals("IX"));
		Assert.assertTrue(RomanNumerals.romanNumeralify(10).equals("X"));
	}

}
