package com.abhi.parking.service.test;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.abhi.parking.service.ParkingLot;

public class MallTest {

	@Test
	public void testMallScooter() {

		ParkingLot parking = new ParkingLot(2, 2, 2, "mall", "src/test/resources/mall1.csv");

		Date curTime = new Date();
		curTime.setHours(curTime.getHours() - 5);
		Assert.assertEquals(1, parking.park("scooter", curTime));
		Assert.assertEquals(2, parking.park("scooter"));

		// no space left
		Assert.assertEquals(-1, parking.park("scooter"));

		// unpark after 5 hours
		Assert.assertEquals(50, parking.unpark(1), 0.00001);

		// unpark in less than one hour
		Assert.assertEquals(10, parking.unpark(2), 0.00001);

		// spaces is available, park again
		Assert.assertEquals(3, parking.park("scooter"));
	}

	@Test
	public void testMallCar() {

		ParkingLot parking = new ParkingLot(2, 2, 2, "mall", "src/test/resources/mall1.csv");

		Date curTime = new Date();
		curTime.setHours(curTime.getHours() - 5);
		Assert.assertEquals(1, parking.park("car", curTime));
		Assert.assertEquals(2, parking.park("car"));

		// no space left
		Assert.assertEquals(-1, parking.park("car"));

		// unpark after 5 hours
		Assert.assertEquals(100, parking.unpark(1), 0.00001);

		// unpark in less than one hour
		Assert.assertEquals(20, parking.unpark(2), 0.00001);

		// spaces is available, park again
		Assert.assertEquals(3, parking.park("car"));
	}

	@Test
	public void testMallBus() {

		ParkingLot parking = new ParkingLot(2, 2, 2, "mall", "src/test/resources/mall1.csv");

		Date curTime = new Date();
		curTime.setHours(curTime.getHours() - 5);
		Assert.assertEquals(1, parking.park("bus", curTime));
		Assert.assertEquals(2, parking.park("bus"));

		// no space left
		Assert.assertEquals(-1, parking.park("bus"));

		// unpark after 5 hours
		Assert.assertEquals(250, parking.unpark(1), 0.00001);

		// unpark in less than one hour
		Assert.assertEquals(50, parking.unpark(2), 0.00001);

		// spaces is available, park again
		Assert.assertEquals(3, parking.park("bus"));
	}

	@Test
	public void testMallAll() {

		ParkingLot parking = new ParkingLot(1, 1, 1, "mall", "src/test/resources/mall1.csv");

		Date curTime = new Date();
		curTime.setHours(curTime.getHours() - 5);
		Assert.assertEquals(1, parking.park("scooter", curTime));
		Assert.assertEquals(2, parking.park("car"));
		Assert.assertEquals(3, parking.park("bus"));

		// no space left
		Assert.assertEquals(-1, parking.park("scooter"));
		Assert.assertEquals(-1, parking.park("car"));
		Assert.assertEquals(-1, parking.park("bus"));

		// unpark scooter after 5 hours
		Assert.assertEquals(50, parking.unpark(1), 0.00001);

		// unpark in less than one hour
		Assert.assertEquals(20, parking.unpark(2), 0.00001);
		Assert.assertEquals(50, parking.unpark(3), 0.00001);

		// spaces is available, park again
		Assert.assertEquals(4, parking.park("scooter"));
		Assert.assertEquals(5, parking.park("car"));
		Assert.assertEquals(6, parking.park("bus"));
	}
}
