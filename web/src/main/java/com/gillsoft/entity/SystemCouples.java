package com.gillsoft.entity;

import java.io.Serializable;

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
@Table(name = "system_couples")
@NamedQuery(name="SystemCouples.findAll", query="SELECT s FROM SystemCouples s")
@JsonInclude(value = Include.NON_NULL)
@IdClass(SystemCouplesId.class)
public class SystemCouples implements Serializable {

	private static final long serialVersionUID = 113248371447646003L;

	@Id
	@Column(name="system_id", nullable=false)
	@JsonProperty("system_id")
	private Integer systemId;

	@Id
	@Column(name="couple_id", nullable=false)
	@JsonProperty("couple_id")
	private Long coupleId;

	public SystemCouples() {
	}

	public SystemCouples(Integer systemId, Long coupleId) {
		this.systemId = systemId;
		this.coupleId = coupleId;
	}

	public Integer getSystemId() {
		return this.systemId;
	}

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}

	public Long getCoupleId() {
		return this.coupleId;
	}

	public void setCoupleId(Long coupleId) {
		this.coupleId = coupleId;
	}

}