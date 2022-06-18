package com.abhi.parking.utils;

public class NameMapperUtil {

	public static String nameMapper(String val) {
		if (val == null)
			return null;

		if (val.toUpperCase().contains("SCOO") || val.toUpperCase().contains("MOTOR")) {
			return "SCOOTER";
		}
		if (val.toUpperCase().contains("CAR") || val.toUpperCase().contains("SUV")) {
			return "CAR";
		}
		if (val.toUpperCase().contains("BUS") || val.toUpperCase().contains("TRUCK")) {
			return "BUS";
		}

		return "";
	}
}
