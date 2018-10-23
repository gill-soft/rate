package com.gillsoft.model;

import java.math.BigDecimal;

public class Rate {

	String curCode;

	BigDecimal rate;

	public Rate(String curCode, BigDecimal rate) {
		this.curCode = curCode;
		this.rate = rate;
	}

	public String getCurCode() {
		return curCode;
	}

	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

}
