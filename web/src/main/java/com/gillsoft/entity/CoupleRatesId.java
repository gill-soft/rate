package com.gillsoft.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CoupleRatesId implements Serializable {

	private static final long serialVersionUID = 1555902152051060880L;

	private Integer coupleId;

	private BigDecimal rate;

	private Date dateStart;

	private Date dateEnd;

	public CoupleRatesId() {
	}

	public Integer getCoupleId() {
		return coupleId;
	}

	public void setCoupleId(Integer coupleId) {
		this.coupleId = coupleId;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

}