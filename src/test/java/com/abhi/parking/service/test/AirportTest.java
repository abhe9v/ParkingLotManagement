package com.abhi.parking.service.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import com.abhi.parking.service.ParkingLot;

public class AirportTest {

	@Test
	public void testAirportScooter() throws ParseException {

		ParkingLot parking = new ParkingLot(2, 2, 0, "airport", "src/test/resources/airport1.csv");

		Date curTime = new Date();
		curTime.setHours(curTime.getHours() - 5);

		Assert.assertEquals(1, parking.park("scooter", curTime));
		Assert.assertEquals(2, parking.park("scooter"));

		// no space left
		Assert.assertEquals(-1, parking.park("scooter"));

		// unpark after 5 hours
		Assert.assertEquals(40, parking.unpark(1), 0.00001);

		// park and unpark with 3 days difference
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
		Date dateBefore = sdf.parse("06/12/2022");
		Date dateAfter = sdf.parse("06/15/2022");
		Assert.assertEquals(3, parking.park("scooter", dateBefore));
		Assert.assertEquals(240, parking.unpark(3, dateAfter), 0.00001);

		// excelusive end time
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		dateBefore = sdf.parse("2022-06-12 00:00:00.000");
		dateAfter = sdf.parse("2022-06-12 07:59:59.999");
		Assert.assertEquals(4, parking.park("scooter", dateBefore));
		Assert.assertEquals(40, parking.unpark(4, dateAfter), 0.00001);

		dateBefore = sdf.parse("2022-06-12 00:00:00.000");
		dateAfter = sdf.parse("2022-06-12 08:00:00.000");
		Assert.assertEquals(5, parking.park("scooter", dateBefore));
		Assert.assertEquals(60, parking.unpark(5, dateAfter), 0.00001);
	}

	@Test
	public void testAirportCar() throws ParseException {

		ParkingLot parking = new ParkingLot(2, 2, 0, "airport", "src/test/resources/airport1.csv");

		Date curTime = new Date();
		curTime.setHours(curTime.getHours() - 5);

		Assert.assertEquals(1, parking.park("car", curTime));
		Assert.assertEquals(2, parking.park("car"));

		// no space left
		Assert.assertEquals(-1, parking.park("car"));

		// unpark after 5 hours
		Assert.assertEquals(60, parking.unpark(1), 0.00001);

		// park and unpark with 3 days difference
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
		Date dateBefore = sdf.parse("06/12/2022");
		Date dateAfter = sdf.parse("06/15/2022");
		Assert.assertEquals(3, parking.park("car", dateBefore));
		Assert.assertEquals(300, parking.unpark(3, dateAfter), 0.00001);

		// excelusive end time
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		dateBefore = sdf.parse("2022-06-12 00:00:00.000");
		dateAfter = sdf.parse("2022-06-12 11:59:59.999");
		Assert.assertEquals(4, parking.park("car", dateBefore));
		Assert.assertEquals(60, parking.unpark(4, dateAfter), 0.00001);

		dateBefore = sdf.parse("2022-06-12 00:00:00.000");
		dateAfter = sdf.parse("2022-06-12 12:00:00.000");
		Assert.assertEquals(5, parking.park("car", dateBefore));
		Assert.assertEquals(80, parking.unpark(5, dateAfter), 0.00001);
	}

	@Test
	public void testAirportBus() throws ParseException {

		ParkingLot parking = new ParkingLot(2, 2, 0, "airport", "src/test/resources/airport1.csv");

		// no parking space for bus
		Assert.assertEquals(-1, parking.park("bus"));

	}

	@Test
	public void testAirportAll() throws ParseException {

		ParkingLot parking = new ParkingLot(1, 1, 0, "airport", "src/test/resources/airport1.csv");

		Assert.assertEquals(1, parking.park("scooter"));
		Assert.assertEquals(2, parking.park("car"));

		// no space left
		Assert.assertEquals(-1, parking.park("scooter"));
		Assert.assertEquals(-1, parking.park("car"));
		Assert.assertEquals(-1, parking.park("bus"));

		// unpark
		Assert.assertEquals(0, parking.unpark(1), 0.00001);
		Assert.assertEquals(60, parking.unpark(2), 0.00001);

		// unpark invalid token
		Assert.assertEquals(0, parking.unpark(3), 0.00001);

	}

	@Test
	public void testStadiumNegative() throws ParseException {
		ParkingLot parking = new ParkingLot(1, 1, 0, "airport", "src/test/resources/airport1.csv");

		// unpark invalid token
		Assert.assertEquals(0, parking.unpark(3), 0.00001);
	}
}
