package com.combustiblelemons.thrillingtales;

import java.util.Random;

public class Dice {
	private int size;
	// private int spread;
	private int max;
	private int min;

	protected final static String d4 = "d4";
	protected final static String d6 = "d6";
	protected final static String d8 = "d8";
	protected final static String d10 = "d10";
	protected final static String d12 = "d12";
	protected final static String d20 = "d20";
	protected final static String d100 = "d100";
	protected final static String d32 = "d32";

	Dice(int Size) {
		this.size = Size;
		this.max = Size;
		this.min = 1;
	}

	/**
	 * XdY is equivalent of D
	 * 
	 * @param type
	 *            dice written in XdY form where X is number of dices, Y maximum
	 *            value.
	 */
	Dice(String type) {
		int _min = 0;
		int _max = 0;
		String _type = type.toLowerCase();
		if (_type.startsWith("d")) {
			_max = Integer.valueOf(_type.substring(1, _type.length()));
			_min = 1;
		} else if (_type.split("d").length >= 2) {
			String[] _d = _type.split("d");
			_min = Integer.valueOf(_d[0]);
			_max = Integer.valueOf(_d[1]) * _min;
		}
		this.max = _max;
		this.min = _min;
		this.size = _max;
	}

	Dice(int Min, int Max) {
		this.min = Min;
		this.max = Max;
		this.size = Max;
	}

	public int getSize() {
		return this.size;
	}

	public int getMin() {
		return this.min;
	}

	public int getMax() {
		return this.max;
	}

	// public int getSpread(){ return this.spread;}
	// public void setSpread(int Spread){ this.spread = Spread; }

	/**
	 * @return Randomized number from the pool of size of the dice object
	 */
	public int getValue() {
		Random roll = new Random();
		int value = 0;
		value = roll.nextInt(this.size);
		if (value <= this.min) {
			value = this.min;
		}
		roll = null;
		return value;
	}

	public int getRandom(int size) {
		Random roll = new Random();
		int value = 0;
		value = roll.nextInt(size);
		if (value <= this.min) {
			value = this.min;
		}
		roll = null;
		return value;
	}

	public Integer[] getPool(int amount, int maxValue) {
		Random roll = new Random();
		Integer[] pool = new Integer[amount];
		for (int i = 0; i < amount; i++) {
			int value = roll.nextInt(maxValue);
			if (value <= this.min) {
				value = this.min;
			}
			pool[i] = value;
		}
		roll = null;
		return pool;
	}
}
