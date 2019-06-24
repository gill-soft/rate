package com.gillsoft.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "couple_rates")
@NamedQuery(name="CoupleRates.findAll", query="SELECT s FROM CoupleRates s")
@JsonInclude(value = Include.NON_NULL)
@IdClass(CoupleRatesId.class)
public class CoupleRates implements Serializable {

	private static final long serialVersionUID = -4974653223542590795L;

	@Id
	@Column(name="couple_id", nullable=false)
	@JsonProperty("couple_id")
	private Integer coupleId;

	@Id
	@Column(name="rate", nullable=false)
	private BigDecimal rate;

	@Id
	@Column(name="date_start", nullable=false)
	@JsonProperty("date_start")
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date dateStart;

	@Id
	@Column(name="date_end", nullable=true)
	@JsonProperty("date_end")
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
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

	@PrePersist
	@PreUpdate
    public void prePersistUpdate() {
		if (dateEnd == null) {
			dateEnd = new GregorianCalendar(2099, Calendar.DECEMBER, 31, 23, 59, 59).getTime();
		}
    }
 
}