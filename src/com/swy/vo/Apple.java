package com.swy.vo;

public class Apple {
	Dot middledot;
	public Dot getMiddledot() {
		return middledot;
	}
	public void setMiddledot(Dot middledot) {
		this.middledot = middledot;
	}
	public float getBanjing() {
		return banjing;
	}
	public void setBanjing(float banjing) {
		this.banjing = banjing;
	}
	float banjing;
	public Apple(Dot middledot, float banjing) {
		super();
		this.middledot = middledot;
		this.banjing = banjing;
	}

}
