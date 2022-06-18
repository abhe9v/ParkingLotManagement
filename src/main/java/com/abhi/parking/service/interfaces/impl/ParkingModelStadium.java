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

public class ParkingModelStadium implements ParkingModel {

	Map<String, int[]> feeConfig = null;
	int perHourStartsAt = 0;

	public ParkingModelStadium(String fareFile) {
		feeConfig = readMallInputEntries(fareFile);
	}

	public double calcFare(ParkedInfo pinfo, Date end) {
		long timeDiff = end.getTime() - pinfo.getParkedOn().getTime();
		double hours = TimeUnit.HOURS.convert(timeDiff, TimeUnit.MILLISECONDS);
		if (hours >= perHourStartsAt) {
			double tillPerHour = feeConfig.get(pinfo.getVtype().name())[perHourStartsAt - 1];
			double afterPerHour = (hours - perHourStartsAt + 1)
					* feeConfig.get(pinfo.getVtype().name())[perHourStartsAt];
			return tillPerHour + afterPerHour;
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
			int prev = 0;
			while ((line = br.readLine()) != null) {
				String[] cols = line.split(splitBy);
				if (!headerSkipped) {
					headerSkipped = true;
					continue;
				}

				if (!perHourFares.containsKey(NameMapperUtil.nameMapper(cols[0]))) {
					prev = 0;
					perHourFares.put(NameMapperUtil.nameMapper(cols[0]), new int[26]);
				}
				int[] fares = perHourFares.get(NameMapperUtil.nameMapper(cols[0]));

				if (cols[1].toLowerCase().contains("infinity")) {
					String[] times = cols[1].split("-");
					perHourStartsAt = Integer.valueOf(times[0].trim());
					fares[perHourStartsAt] = Integer.valueOf(cols[2].trim());
				} else {
					String[] times = cols[1].split("-");
					int fromTime = Integer.valueOf(times[0].trim());
					int toTime = 24;
					if (!cols[1].toLowerCase().contains("infinity")) {
						toTime = Integer.valueOf(times[1].trim());
					}
					for (int k = fromTime; k <= toTime; k++) {
						fares[k] = prev + Integer.valueOf(cols[2].trim());
					}
					prev = fares[fromTime];
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return perHourFares;
	}

}
