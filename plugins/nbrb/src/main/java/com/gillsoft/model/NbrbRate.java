package com.gillsoft.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NbrbRate {

	@JsonProperty("Cur_ID")
	private int curId;

	@JsonProperty("Cur_Abbreviation")
	private String curAbbreviation;

	@JsonProperty("Cur_OfficialRate")
	private BigDecimal curOfficialRate;

	@JsonProperty("Cur_Scale")
	private BigDecimal curScale;

	public int getCurId() {
		return curId;
	}

	public void setCurId(int curId) {
		this.curId = curId;
	}

	public String getCurAbbreviation() {
		return curAbbreviation;
	}

	public void setCurAbbreviation(String curAbbreviation) {
		this.curAbbreviation = curAbbreviation;
	}

	public BigDecimal getCurOfficialRate() {
		return curOfficialRate;
	}

	public void setCurOfficialRate(BigDecimal curOfficialRate) {
		this.curOfficialRate = curOfficialRate;
	}

	public BigDecimal getCurScale() {
		return curScale;
	}

	public void setCurScale(BigDecimal curScale) {
		this.curScale = curScale;
	}
	
	public BigDecimal getRate() {
		return this.curScale.equals(BigDecimal.ONE)
				? this.curOfficialRate : this.curOfficialRate.divide(this.curScale);
	}

}