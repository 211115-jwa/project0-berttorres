package com.revature.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.revature.beans.Bike;
import com.revature.data.postgres.BikePostgres;

public class BikeDAOTest {
	private BikeDAO bikeDao = new BikePostgres();

	// test 1 and 2 test the id when it exists and when it doesn't
	@Test
	public void getByIdWhenIdExists() {
		// setup
		int idInput = 1;
		// call the method we're testing
		Bike idOutput = bikeDao.getById(idInput);
		// assert that it did what we expected
		assertEquals(1, idOutput.getId());
	}

	@Test
	public void getByIdWhenIdDoesNotExists() {
		int idInput = -1;
		Bike bikeOutput = bikeDao.getById(idInput);
		assertNull(bikeOutput);
	}
	
	// tests to get all bikes
	@Test
	public void getAll() {
		Set<Bike> allBikes = bikeDao.getAll();
		assertNotNull(allBikes);
	}
	
	// tests the create a bike method by checking if an ID was generated
	@Test
	public void create() {
		Bike newBike = new Bike();
		System.out.println(newBike);

		int generatedId = bikeDao.create(newBike);

		assertNotEquals(0, generatedId);
		System.out.print("Generated ID = ");
		System.out.println(generatedId);
	}

}
