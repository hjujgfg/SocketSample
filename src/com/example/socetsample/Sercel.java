package com.example.socetsample;

import java.io.Serializable;

public class Sercel implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 234L;
	public float field1, field2; 
	public Sercel (float r, float as) {
		field1 = r;
		field2 = as;
	}
	@Override
	public String toString() {
		return field1 +" ^ " + field2;		
	}
	@Override
	public Sercel clone() {
		return new Sercel(field1, field2);
	}
}
