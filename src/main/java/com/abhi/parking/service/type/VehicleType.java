package com.abhi.parking.service.type;

public enum VehicleType {
	SCOOTER("Motorcycles/Scooters"), CAR("Cars/SUVs"), BUS("Buses/Trucks");

	private String description;

	private VehicleType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
