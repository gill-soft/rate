package com.gillsoft.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "couple_rates")
@NamedQuery(name="CoupleRates.findAll", query="SELECT s FROM CoupleRates s")
@JsonInclude(value = Include.NON_NULL)
@IdClass(CoupleRatesId.class)
@Access(AccessType.PROPERTY)
public class CoupleRates implements Serializable {

	private static final long serialVersionUID = -4974653223542590795L;

	@Id
	@Column(name="couple_id", nullable=false)
	@JsonProperty("couple_id")
	private Integer coupleId;

	@Column(name="rate", nullable=false)
	private BigDecimal rate;

	@Column(name="date_start", nullable=false)
	@JsonProperty("date_start")
	private Date dateStart;

	@Column(name="date_end", nullable=true)
	@JsonProperty("date_end")
	private Date dateEnd;

	public CoupleRates() {
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