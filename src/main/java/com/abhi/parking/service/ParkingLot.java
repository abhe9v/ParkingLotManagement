package com.abhi.parking.service;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.abhi.parking.service.beans.ParkedInfo;
import com.abhi.parking.service.interfaces.ParkingModel;
import com.abhi.parking.service.interfaces.impl.ParkingModelAirport;
import com.abhi.parking.service.interfaces.impl.ParkingModelMall;
import com.abhi.parking.service.interfaces.impl.ParkingModelStadium;
import com.abhi.parking.service.type.VehicleType;

public class ParkingLot {

	private Map<VehicleType, Integer> totalSpaces = new HashMap<VehicleType, Integer>();
	private Map<VehicleType, Integer> availableSpaces = new HashMap<VehicleType, Integer>();
	private Map<Long, ParkedInfo> parkedInfo = new HashMap<Long, ParkedInfo>();
	private ParkingModel feeStrategy;
	long tokenCounter = 1l;

	/**
	 * Create parking lot.
	 * 
	 * @param scooterTotal Total scooter and motorcycle spaces
	 * @param carTotal     Total car and SUV spaces
	 * @param busTotal     Total bus and truck spaces
	 * @param model        Fee model. options - mall, airport, stadium.
	 * @param fareJsonPath Fee model configuration file.
	 */
	public ParkingLot(int scooterTotal, int carTotal, int busTotal, String model, String fareJsonPath) {

		this.totalSpaces.put(VehicleType.SCOOTER, scooterTotal);
		this.totalSpaces.put(VehicleType.CAR, carTotal);
		this.totalSpaces.put(VehicleType.BUS, busTotal);

		this.availableSpaces.put(VehicleType.SCOOTER, scooterTotal);
		this.availableSpaces.put(VehicleType.CAR, carTotal);
		this.availableSpaces.put(VehicleType.BUS, busTotal);

		try {
			this.setFeeStrategy(model, fareJsonPath);
		} catch (Exception e) {
			System.out.println("ERROR : Failed to create fee model." + e.getMessage());
		}
	}

	/**
	 * Park vehicle.
	 * 
	 * @param vehicleType Options = scooter for scooter or motorcycle, car for car
	 *                    or SUV, bus for bus or truck.
	 * @return Parking token number.
	 */
	public synchronized long park(String vehicleType) {
		return park(vehicleType, new Date());
	}

	/**
	 * Unpark vehicle.
	 * 
	 * @param token Token number received at parking time.
	 * @return Parking fee charges.
	 */
	public double unpark(long token) {
		Date curTime = new Date();
		return unpark(token, curTime);
	}

	/**
	 * NOTE: DO NOT USE. Method created for unit testing purpose.
	 */
	public long park(String vehicleType, Date parkTime) {
		VehicleType vtype = VehicleType.valueOf(vehicleType.toUpperCase());
		if (vtype == null) {
			System.out.println("ERROR : Invalid vehicle type provided.");
			return -1;
		}

		int avlSpaces = availableSpaces.get(vtype);
		if (avlSpaces > 0) {
			availableSpaces.put(vtype, --avlSpaces);
			ParkedInfo pi = new ParkedInfo(tokenCounter, parkTime, vtype);
			System.out.println("INFO : Parking info for vehicle token: " + tokenCounter + ", Time : " + pi.getParkedOn()
					+ ", Type : " + vtype.name());
			parkedInfo.put(tokenCounter, pi);
			return tokenCounter++;
		}

		System.out.println("INFO : Parking space not available, Type : " + vtype.name());
		return -1;
	}

	/**
	 * NOTE: DO NOT USE. Method created for unit testing purpose.
	 */
	public double unpark(long token, Date unParkTime) {
		if (!parkedInfo.containsKey(token)) {
			System.out.println("ERROR : Invalid token: " + token);
			return 0l;
		}
		int avlSpaces = availableSpaces.get(parkedInfo.get(token).getVtype());
		availableSpaces.put(parkedInfo.get(token).getVtype(), ++avlSpaces);
		double fee = feeStrategy.calcFare(parkedInfo.get(token), unParkTime);
		System.out.println("INFO : Unarked. Token = " + token + " , fee : " + fee);
		parkedInfo.remove(token);
		return fee;
	}

	// set fee model and load fee configuration
	private void setFeeStrategy(String model, String fareFilePath) throws Exception {

		ParkingModel parkModel = null;

		if (model == null) {
			System.out.println("ERROR : Provide fee model.");
		}
		if (fareFilePath == null || !(new File(fareFilePath).exists())) {
			System.out.println("ERROR : Provide valid fee model JSON.");
		}

		try {
			if ("mall".contains(model.toLowerCase())) {
				parkModel = new ParkingModelMall(fareFilePath);
			} else if ("airport".contains(model.toLowerCase())) {
				parkModel = new ParkingModelAirport(fareFilePath);
			} else if ("stadium".contains(model.toLowerCase())) {
				parkModel = new ParkingModelStadium(fareFilePath);
			} else {
				System.out.println("ERROR : Invalid model name provided.");
				throw new Exception("ERROR : Invalid model name provided.");
			}
		} catch (Exception e) {
			throw e;
		}

		this.feeStrategy = parkModel;
	}

}
