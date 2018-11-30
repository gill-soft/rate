package com.gillsoft.entity;

import java.io.Serializable;

public class SystemCouplesId implements Serializable {

	private static final long serialVersionUID = 7711503418172141042L;

	private Integer systemId;

	private Long coupleId;

	public SystemCouplesId() {
	}

	public Integer getSystemId() {
		return systemId;
	}

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}

	public Long getCoupleId() {
		return coupleId;
	}

	public void setCoupleId(Long coupleId) {
		this.coupleId = coupleId;
	}

}