package com.abhi.parking.service.interfaces;

import java.util.Date;

import com.abhi.parking.service.beans.ParkedInfo;

public interface ParkingModel {

	public double calcFare(ParkedInfo pinfo, Date end);

	public void setFeeConfig(String fareFile);
}
