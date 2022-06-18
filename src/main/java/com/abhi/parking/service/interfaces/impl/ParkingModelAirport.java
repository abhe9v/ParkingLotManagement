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

public class ParkingModelAirport implements ParkingModel {

	Map<String, int[]> feeConfig = null;

	public ParkingModelAirport(String fareFile) {
		feeConfig = readMallInputEntries(fareFile);
	}

	public double calcFare(ParkedInfo pinfo, Date end) {
		long timeDiff = end.getTime() - pinfo.getParkedOn().getTime();
		double hours = TimeUnit.HOURS.convert(timeDiff, TimeUnit.MILLISECONDS);
		long days = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
		if (days > 0) {
			return days * feeConfig.get(pinfo.getVtype().name())[25];
		}
		return feeConfig.get(pinfo.getVtype().name())[(int) hours];
	}

	public void setFeeConfig(String fareFile) {
		feeConfig = readMallInputEntries(fareFile);
	}

	private Map<String, int[]> readMallInputEntries(String entriesFile) {
		String line = "";
		String splitBy = ",";
		Map<String, int[]> perHourFares = new HashMap<String, int[]>();
		boolean headerSkipped = false;
		try {
			BufferedReader br = new BufferedReader(new FileReader(entriesFile));
			while ((line = br.readLine()) != null) {
				String[] cols = line.split(splitBy);
				if (!headerSkipped) {
					headerSkipped = true;
					continue;
				}

				if (!perHourFares.containsKey(NameMapperUtil.nameMapper(cols[0]))) {
					perHourFares.put(NameMapperUtil.nameMapper(cols[0]), new int[26]);
				}
				int[] fares = perHourFares.get(NameMapperUtil.nameMapper(cols[0]));

				if (cols[1].toLowerCase().contains("day")) {
					fares[25] = Integer.valueOf(cols[2].trim());
				} else {
					String[] times = cols[1].split("-");
					int fromTime = Integer.valueOf(times[0].trim());
					int toTime = Integer.valueOf(times[1].trim());
					for (int k = fromTime; k <= toTime; k++) {
						fares[k] = Integer.valueOf(cols[2].trim());
					}
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return perHourFares;
	}

}
