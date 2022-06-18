package com.abhi.parking.service.beans;

import java.util.Date;

import com.abhi.parking.service.type.VehicleType;

public class ParkedInfo {

	private long token;
	private Date parkedOn;
	private VehicleType vtype;

	public ParkedInfo(long token, Date parkedOn, VehicleType vtype) {
		this.token = token;
		this.parkedOn = parkedOn;
		this.vtype = vtype;
	}

	public long getToken() {
		return token;
	}

	public void setToken(long token) {
		this.token = token;
	}

	public Date getParkedOn() {
		return parkedOn;
	}

	public void setParkedOn(Date parkedOn) {
		this.parkedOn = parkedOn;
	}

	public VehicleType getVtype() {
		return vtype;
	}

	public void setVtype(VehicleType vtype) {
		this.vtype = vtype;
	}

}
