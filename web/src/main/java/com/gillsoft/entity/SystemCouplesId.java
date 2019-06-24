package com.gillsoft.entity;

import java.io.Serializable;

public class SystemCouplesId implements Serializable {

	private static final long serialVersionUID = 7711503418172141042L;

	private Integer systemId;

	private Integer coupleId;

	public SystemCouplesId() {
	}

	public Integer getSystemId() {
		return systemId;
	}

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}

	public Integer getCoupleId() {
		return coupleId;
	}

	public void setCoupleId(Integer coupleId) {
		this.coupleId = coupleId;
	}

}