package com.abhi.parking.service.interfaces.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.abhi.parking.service.beans.ParkedInfo;
import com.abhi.parking.service.interfaces.ParkingModel;
import com.abhi.parking.utils.NameMapperUtil;

public class ParkingModelMall implements ParkingModel {

	Map<String, Integer> feeConfig = null;

	public ParkingModelMall(String fareFile) {
		feeConfig = readMallInputEntries(fareFile);
	}

	public double calcFare(ParkedInfo pinfo, Date end) {
		long timeDiff = end.getTime() - pinfo.getParkedOn().getTime();
		double hours = TimeUnit.HOURS.convert(timeDiff, TimeUnit.MILLISECONDS);
		if (hours < 1) { // someone leaving in less than hour
			hours = 1;
		}
		int inthours = (int) Math.ceil(hours);
		return inthours * feeConfig.get(pinfo.getVtype().name());
	}

	public void setFeeConfig(String fareFile) {
		feeConfig = readMallInputEntries(fareFile);
	}

	private Map<String, Integer> readMallInputEntries(String entriesFile) {
		String line = "";
		String splitBy = ",";
		Map<String, Integer> perHourFares = new HashMap<String, Integer>();
		boolean headerSkipped = false;
		try {
			BufferedReader br = new BufferedReader(new FileReader(entriesFile));
			while ((line = br.readLine()) != null) {
				String[] cols = line.split(splitBy);
				if (!headerSkipped) {
					headerSkipped = true;
					continue;
				}
				perHourFares.put(NameMapperUtil.nameMapper(cols[0]), Integer.valueOf(cols[1].trim()));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return perHourFares;
	}

}
